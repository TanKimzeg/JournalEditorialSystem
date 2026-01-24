package top.tankimzeg.editorial_system.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import top.tankimzeg.editorial_system.dto.request.RevisionDTO;
import top.tankimzeg.editorial_system.dto.response.RevisionVO;
import top.tankimzeg.editorial_system.entity.Revision;

/**
 * @author Kim
 * @date 2026/1/23 18:54
 * @description 评审记录映射器
 */
@Mapper
public interface RevisionRecordMapper {

    RevisionRecordMapper INSTANCE = Mappers.getMapper(RevisionRecordMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "processId", source = "process.id")
    @Mapping(target = "manuscriptId", source = "process.manuscript.id")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.realName")
    @Mapping(target = "attachmentIds", expression = "java(entity.getAttachments().stream().map(att -> att.getId()).toList())")
    RevisionVO entityToVO(Revision entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "process", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    @Mapping(target = "finishedAt", ignore = true)
    Revision dtoToEntity(RevisionDTO dto);
}
