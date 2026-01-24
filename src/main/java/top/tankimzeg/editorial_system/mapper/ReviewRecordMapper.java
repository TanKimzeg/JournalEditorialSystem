package top.tankimzeg.editorial_system.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import top.tankimzeg.editorial_system.dto.request.ReviewDTO;
import top.tankimzeg.editorial_system.dto.response.ReviewVO;
import top.tankimzeg.editorial_system.entity.Review;

/**
 * @author Kim
 * @date 2026/1/23 18:54
 * @description 评审记录映射器
 */
@Mapper
public interface ReviewRecordMapper {

    ReviewRecordMapper INSTANCE = Mappers.getMapper(ReviewRecordMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "processId", source = "process.id")
    @Mapping(target = "manuscriptId", source = "process.manuscript.id")
    @Mapping(target = "manuscriptTitle", source = "process.manuscript.title")
    @Mapping(target = "reviewerId", source = "reviewer.author.id")
    @Mapping(target = "reviewerName", expression = "java(entity.getReviewer().getAuthor().getRealName() != null ? entity.getReviewer().getAuthor().getRealName() : entity.getReviewer().getAuthor().getUsername())")
    @Mapping(target = "decision", expression = "java(entity.getDecision() != null ? entity.getDecision().name() : null)")
    @Mapping(target = "attachmentIds", expression = "java(entity.getAttachments().stream().map(att -> att.getId()).toList())")
    ReviewVO entityToVO(Review entity);

    // dto -> entity: only map decision and comments; ignore server-managed relations and timestamps
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "process", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "finishedAt", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    Review dtoToEntity(ReviewDTO dto);
}
