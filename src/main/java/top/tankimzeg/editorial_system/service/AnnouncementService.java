package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.tankimzeg.editorial_system.dto.request.AnnouncementRequest;
import top.tankimzeg.editorial_system.dto.response.AnnouncementVO;
import top.tankimzeg.editorial_system.entity.Announcement;
import top.tankimzeg.editorial_system.exception.BusinessException;
import top.tankimzeg.editorial_system.mapper.AnnouncementMapper;
import top.tankimzeg.editorial_system.repository.AnnouncementRepo;
import top.tankimzeg.editorial_system.service.impl.MediaStorageService;

import java.time.LocalDateTime;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepo announcementRepo;

    @Autowired
    private MediaStorageService storageService;

    private static final AnnouncementMapper ANNOUNCEMENT_MAPPER = AnnouncementMapper.INSTANCE;

    @Transactional
    public AnnouncementVO create(AnnouncementRequest request) {
        Announcement entity = ANNOUNCEMENT_MAPPER.requestToEntity(request);
        applyPublishState(entity, request.getPublish());
        return ANNOUNCEMENT_MAPPER.entityToVO(announcementRepo.save(entity));
    }

    @Transactional
    public AnnouncementVO update(Long id, AnnouncementRequest request) {
        Announcement entity = findById(id);
        ANNOUNCEMENT_MAPPER.updateEntityFromRequest(request, entity);
        applyPublishState(entity, request.getPublish());
        return ANNOUNCEMENT_MAPPER.entityToVO(announcementRepo.save(entity));
    }

    @Transactional
    public AnnouncementVO updatePublishStatus(Long id, Boolean published) {
        Announcement entity = findById(id);
        applyPublishState(entity, published);
        return ANNOUNCEMENT_MAPPER.entityToVO(announcementRepo.save(entity));
    }

    @Transactional(readOnly = true)
    public AnnouncementVO getManagedOne(Long id) {
        return ANNOUNCEMENT_MAPPER.entityToVO(findById(id));
    }

    @Transactional(readOnly = true)
    public AnnouncementVO getPublishedOne(Long id) {
        Announcement announcement = announcementRepo.findById(id)
                .filter(a -> a.getStatus() == Announcement.Status.PUBLISHED)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "未找到已发布公告: " + id));
        return ANNOUNCEMENT_MAPPER.entityToVO(announcement);
    }

    @Transactional(readOnly = true)
    public Page<AnnouncementVO> listForEditors(Pageable pageable) {
        return announcementRepo.findAll(pageable).map(ANNOUNCEMENT_MAPPER::entityToVO);
    }

    @Transactional(readOnly = true)
    public Page<AnnouncementVO> listPublished(Pageable pageable) {
        Pageable sortedPage = pageable.getSort().isUnsorted()
                ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "publishedAt"))
                : pageable;
        return announcementRepo
                .findByStatus(Announcement.Status.PUBLISHED, sortedPage)
                .map(ANNOUNCEMENT_MAPPER::entityToVO);
    }

    @Transactional
    public void delete(Long id) {
        Announcement entity = findById(id);
        entity.setStatus(Announcement.Status.ARCHIVED); // 先标记为已归档，保留数据
        announcementRepo.save(entity);
    }

    @Transactional
    public String uploadPicture(MultipartFile picture) {
        // TODO: 此处返回的是文件系统中的路径，应修改为图床URL
        return storageService.store(picture);
    }

    private Announcement findById(Long id) {
        return announcementRepo.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "未找到公告: " + id));
    }

    private void applyPublishState(Announcement entity, Boolean publish) {
        boolean shouldPublish = Boolean.TRUE.equals(publish);
        if (shouldPublish) {
            entity.setStatus(Announcement.Status.PUBLISHED);
            entity.setPublishedAt(LocalDateTime.now());
        } else {
            entity.setStatus(Announcement.Status.DRAFT);
            entity.setPublishedAt(null);
        }
    }
}
