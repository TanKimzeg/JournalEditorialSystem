package top.tankimzeg.editorial_system.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import top.tankimzeg.editorial_system.dto.request.ManuscriptDTO;
import top.tankimzeg.editorial_system.dto.response.ManuscriptVO;
import top.tankimzeg.editorial_system.entity.Manuscript;

/**
 * @author Kim
 * @date 2026/1/22 21:29
 * @description 稿件映射器
 */
@Mapper
public interface ManuscriptMapper {

    ManuscriptMapper INSTANCE = Mappers.getMapper(ManuscriptMapper.class);

    // create mapping: ignore server-managed or foreign key fields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "manuscriptProcesses", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    Manuscript dtoToEntity(ManuscriptDTO dto);

    // patch/update mapping: ignore nulls so they don't overwrite; also ignore server-managed fields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "manuscriptProcesses", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    void updateEntityFromDto(ManuscriptDTO dto, @MappingTarget Manuscript entity);

    // entity -> vo mapping
    @Mapping(target = "attachmentIds", expression =
            "java(entity.getAttachments().stream().map(att -> att.getId()).toList())")
    @Mapping(target = "authors", expression =
            "java(java.util.Collections.singletonList(entity.getAuthor().getRealName()))")
    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    ManuscriptVO entityToVO(Manuscript entity);

}
