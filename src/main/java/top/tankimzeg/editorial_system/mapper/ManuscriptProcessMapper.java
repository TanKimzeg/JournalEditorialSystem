package top.tankimzeg.editorial_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import top.tankimzeg.editorial_system.dto.response.ManuscriptProcessVO;
import top.tankimzeg.editorial_system.entity.ManuscriptProcess;
import top.tankimzeg.editorial_system.repository.ReviewRepo;
import top.tankimzeg.editorial_system.repository.RevisionRepo;


import java.time.format.DateTimeFormatter;

/**
 * @author Kim
 * @date 2026/1/23 15:41
 * @description 稿件处理流程映射器
 */
@Mapper
public interface ManuscriptProcessMapper {

    ManuscriptProcessMapper INSTANCE = Mappers.getMapper(ManuscriptProcessMapper.class);

    default ManuscriptProcessVO entityToVO(
            ManuscriptProcess entity, @Autowired RevisionRepo revisionRepo, ReviewRepo reviewRepo) {
        if (entity == null)
            return null;
        ManuscriptProcessVO vo = new ManuscriptProcessVO();
        vo.setId(entity.getId());
        vo.setStage(entity.getStage().name());
        vo.setProcessedAt(entity.getProcessedAt().format(DateTimeFormatter.ISO_DATE));
        vo.setFinishedAt(entity.getFinishedAt() != null ?
                entity.getFinishedAt().format(DateTimeFormatter.ISO_DATE) : null);
        vo.setComments(entity.getComments());
        vo.setProcessedByRole(entity.getProcessedBy().getRole().name());
        switch (entity.getStage()) {
            case REVISION -> revisionRepo.findRevisionByProcessId(entity.getId()).getAttachments().forEach(
                    attachment -> vo.getAttachments().add(attachment.getId())
            );

            case PEER_REVIEW -> reviewRepo.findReviewByProcessId(entity.getId()).getAttachments().forEach(
                    attachment -> vo.getAttachments().add(attachment.getId())
            );

        }
        return vo;
    }
}
