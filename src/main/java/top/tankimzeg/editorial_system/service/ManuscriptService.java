package top.tankimzeg.editorial_system.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.tankimzeg.editorial_system.dto.request.ManuscriptDTO;
import top.tankimzeg.editorial_system.dto.response.ManuscriptProcessVO;
import top.tankimzeg.editorial_system.dto.response.ManuscriptVO;
import top.tankimzeg.editorial_system.entity.Manuscript;
import top.tankimzeg.editorial_system.entity.ManuscriptAttachment;
import top.tankimzeg.editorial_system.entity.ManuscriptProcess;
import top.tankimzeg.editorial_system.entity.User;
import top.tankimzeg.editorial_system.events.ManuscriptSubmittedEvent;
import top.tankimzeg.editorial_system.exception.BusinessException;
import top.tankimzeg.editorial_system.mapper.ManuscriptMapper;
import top.tankimzeg.editorial_system.mapper.ManuscriptProcessMapper;
import top.tankimzeg.editorial_system.repository.*;
import top.tankimzeg.editorial_system.service.impl.ManuAttachmentStorageService;

import java.io.IOException;
import java.util.List;

/**
 * @author Kim
 * @date 2026/1/19 12:02
 * @description 稿件服务类
 */
@Slf4j
@Service
public class ManuscriptService {

    @Autowired
    private ManuscriptRepo manuscriptRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RevisionRepo revisionRepo;

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private ManuscriptAttachmentRepo manuscriptAttachmentRepo;

    @Autowired
    private ManuAttachmentStorageService fileStorageService;

    @Autowired
    private ManuscriptProcessRepo manuscriptProcessRepo;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private static final ManuscriptMapper manuscriptMapper = ManuscriptMapper.INSTANCE;
    private static final ManuscriptProcessMapper manuscriptProcessMapper = ManuscriptProcessMapper.INSTANCE;

    @Transactional
    public ManuscriptVO submitManuscript(ManuscriptDTO manuscriptDTO, Long authorId, List<MultipartFile> attachmentFiles) {
        User author = userRepo.findById(authorId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "作者不存在"));
        Manuscript manuscript = manuscriptMapper.dtoToEntity(manuscriptDTO);
        manuscript.setAuthor(author);
        manuscript.setStatus(Manuscript.ManuscriptStatus.SUBMITTED);

        Manuscript savedEntity = manuscriptRepo.save(manuscript);
        if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
            try {
                fileStorageService.store(attachmentFiles, savedEntity);
            } catch (IOException e) {
                log.error("Failed to store manuscript attachments on submit, authorId={}, manuscriptId={}", authorId, savedEntity.getId(), e);
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "附件保存失败，请稍后重试");
            }
        }
        // publish event; handled AFTER_COMMIT by NotificationEventListener
        eventPublisher.publishEvent(new ManuscriptSubmittedEvent(savedEntity));
        return manuscriptMapper.entityToVO(savedEntity);
    }

    public List<ManuscriptVO> getManuscriptsByAuthorId(Long authorId) {
        List<Manuscript> manuscripts = manuscriptRepo.findByAuthorId(authorId);
        // 按提交时间降序排序
        return manuscripts.stream().sorted(
                        (m1, m2) -> m2.getSubmittedAt().compareTo(m1.getSubmittedAt())
                )
                .map(manuscriptMapper::entityToVO).toList();
    }

    @Transactional
    public ManuscriptVO updateManuscript(
            Long manuscriptId, ManuscriptDTO patchedManuscript, List<MultipartFile> attachmentFiles) {
        Manuscript manuscript = manuscriptRepo.findById(manuscriptId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "稿件不存在"));
        manuscriptMapper.updateEntityFromDto(patchedManuscript, manuscript);
        return saveManuscriptAndAttachments(attachmentFiles, manuscript);
    }

    private ManuscriptVO saveManuscriptAndAttachments(List<MultipartFile> attachmentFiles, Manuscript manuscript) {
        Manuscript saved = manuscriptRepo.save(manuscript);
        if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
            try {
                fileStorageService.store(attachmentFiles, saved);
            } catch (IOException e) {
                log.error("Failed to store manuscript attachments, manuscriptId={}", saved.getId(), e);
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "附件保存失败，请稍后重试");
            }
        }
        return manuscriptMapper.entityToVO(saved);
    }

    public byte[] downloadAttachment(String storageKey) {
        try {
            return fileStorageService.load(storageKey);
        } catch (IOException | SecurityException e) {
            log.error("Failed to download attachment, storageKey={}", storageKey, e);
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "附件下载失败，请稍后重试");
        }
    }

    public Manuscript getManuscriptById(Long manuscriptId) {
        return manuscriptRepo.findById(manuscriptId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "稿件不存在"));
    }

    /**
     * 获取稿件的处理流程记录，按处理时间降序排序
     *
     * @param manuscriptId 稿件ID
     * @return 处理流程记录列表
     */
    public List<ManuscriptProcessVO> getManuscriptProcesses(Long manuscriptId) {
        return manuscriptProcessRepo.findByManuscriptId(manuscriptId).stream()
                .sorted((p1, p2) ->
                        p2.getProcessedAt().compareTo(p1.getProcessedAt()))
                .map(p -> manuscriptProcessMapper.entityToVO(p, revisionRepo, reviewRepo))
                .toList();

    }

    /**
     * 获取稿件的最新处理流程记录
     *
     * @param manuscriptId 稿件ID
     * @return 最新的处理流程记录
     */
    public ManuscriptProcess getLatestManuscriptProcess(Long manuscriptId) {
        return manuscriptProcessRepo.findByManuscriptId(manuscriptId).stream()
                .max((p1, p2) ->
                        p1.getProcessedAt().compareTo(p2.getProcessedAt()))
                .orElse(null);
    }

    @Transactional
    public ManuscriptVO publishManuscript(Long manuscriptId) {
        Manuscript manuscript = manuscriptRepo.findById(manuscriptId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "稿件不存在"));
        if (!manuscript.getStatus().equals(Manuscript.ManuscriptStatus.ACCEPTED)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "只有已接受的稿件才能发表");
        }
        manuscript.setStatus(Manuscript.ManuscriptStatus.PUBLISHED);

        // 这里可以添加更多发表相关的逻辑，比如生成DOI，通知作者，排版校对等
        return manuscriptMapper.entityToVO(manuscriptRepo.save(manuscript));
    }

    @Transactional
    public ManuscriptVO withdrawManuscript(Long manuscriptId) {
        Manuscript manuscript = manuscriptRepo.findById(manuscriptId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "稿件不存在"));
        if (manuscript.getStatus().equals(Manuscript.ManuscriptStatus.PUBLISHED)) {
            throw new BusinessException(HttpStatus.CONFLICT, "已发表的稿件不能撤回");
        }
        manuscript.setStatus(Manuscript.ManuscriptStatus.WITHDRAWN);
        return manuscriptMapper.entityToVO(manuscriptRepo.save(manuscript));
    }

    public List<String> listAttachmentIds(Long manuscriptId) {
        return manuscriptAttachmentRepo.findByManuscriptId(manuscriptId)
                .stream().map(ManuscriptAttachment::getId).toList();
    }

    public ManuscriptAttachment getAttachment(String attachmentId) {
        return manuscriptAttachmentRepo.findById(attachmentId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "附件不存在"));
    }
}
