package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.tankimzeg.editorial_system.dto.request.RevisionDTO;
import top.tankimzeg.editorial_system.dto.response.ManuscriptProcessVO;
import top.tankimzeg.editorial_system.dto.response.RevisionVO;
import top.tankimzeg.editorial_system.entity.Manuscript;
import top.tankimzeg.editorial_system.entity.ManuscriptProcess;
import top.tankimzeg.editorial_system.entity.Revision;
import top.tankimzeg.editorial_system.mapper.ManuscriptProcessMapper;
import top.tankimzeg.editorial_system.mapper.RevisionRecordMapper;
import top.tankimzeg.editorial_system.repository.ManuscriptProcessRepo;
import top.tankimzeg.editorial_system.repository.ManuscriptRepo;
import top.tankimzeg.editorial_system.repository.RevisionRepo;
import top.tankimzeg.editorial_system.service.impl.RevisionAttachmentStorageService;

import java.io.IOException;
import java.util.List;

/**
 * @author Kim
 * @date 2026/1/19 17:01
 * @description 修订服务类
 */
@Service
public class RevisionService {

    @Autowired
    private RevisionRepo revisionRepo;

    @Autowired
    private ManuscriptProcessRepo manuscriptProcessRepo;

    @Autowired
    private ManuscriptRepo manuscriptRepo;

    @Autowired
    private RevisionAttachmentStorageService fileStorageService;

    private static final ManuscriptProcessMapper processMapper = ManuscriptProcessMapper.INSTANCE;

    private static final RevisionRecordMapper revisionMapper = RevisionRecordMapper.INSTANCE;

    /**
     * 完成修订并保存修订内容，作者完成修订后调用此方法
     *
     * @param processId   稿件处理流程ID
     * @param revisionDTO 修订内容
     * @return 更新后的稿件处理流程记录
     */
    @Transactional
    public RevisionVO finishedRevision(Long processId, RevisionDTO revisionDTO, List<MultipartFile> files) {
        // 获取最新一条稿件处理流程记录，即分配修订的记录
        ManuscriptProcess process = manuscriptProcessRepo.getReferenceById(processId);
        if (!process.getStage().equals(ManuscriptProcess.Stage.REVISION))
            throw new RuntimeException("当前流程阶段不允许作者完成修订");

        process.setComments(revisionDTO.getComments());
        manuscriptProcessRepo.save(process);
        // 关联修订记录和稿件处理流程
        Revision revision = revisionMapper.dtoToEntity(revisionDTO);
        revision.setAuthor(process.getProcessedBy());
        revision.setProcess(process);
        revision.setFinishedAt(java.time.LocalDateTime.now());
        Revision saved = revisionRepo.save(revision);
        try {
            fileStorageService.store(files, saved);
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败", e);
        }
        // 更新稿件状态为修订完成
        Manuscript manuscript = process.getManuscript();
        // 保存稿件处理流程记录
        manuscriptProcessRepo.save(process);
        manuscript.setStatus(Manuscript.ManuscriptStatus.REVISION_COMPLETED);
        // 保存更新后的稿件
        manuscriptRepo.save(manuscript);
        return revisionMapper.entityToVO(saved);
    }

    public List<ManuscriptProcessVO> getPendingRevisionsByAuthorId(Long authorId) {
        List<ManuscriptProcess> tasks = manuscriptProcessRepo
                .findManuscriptProcessByProcessedBy_Id(authorId);
        return tasks.stream().filter(
                        task -> task.getStage().equals(ManuscriptProcess.Stage.REVISION)
                ).sorted(
                        // 按处理时间降序排序
                        (p1, p2) -> p2.getProcessedAt().compareTo(p1.getProcessedAt())
                ).map(p -> processMapper.entityToVO(p, revisionRepo, null))
                .toList();
    }

}
