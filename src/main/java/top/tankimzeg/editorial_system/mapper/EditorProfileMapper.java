package top.tankimzeg.editorial_system.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.tankimzeg.editorial_system.dto.request.EditorProfileDTO;
import top.tankimzeg.editorial_system.dto.response.EditorProfileVO;
import top.tankimzeg.editorial_system.entity.EditorProfile;
import top.tankimzeg.editorial_system.entity.User;

/**
 * @author Kim
 * @date 2026/1/23 18:14
 * @description 编辑档案映射器
 */
@Mapper
public interface EditorProfileMapper {

    EditorProfileMapper INSTANCE = Mappers.getMapper(EditorProfileMapper.class);

    @Mapping(target = "realName", source = "editor.realName")
    @Mapping(target = "email", source = "editor.email")
    @Mapping(target = "role", expression = "java(entity.getEditor().getRole().name())")
    @Mapping(target = "baseProfile", source = "profile")
    EditorProfileVO entityToVO(EditorProfile entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "profile", source = "dto.baseProfile")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "editor", ignore = true)
    void updateEntityFromDTO(EditorProfileDTO dto, @MappingTarget EditorProfile entity);

    @Mapping(target = "profile", source = "dto.baseProfile")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "editor", source = "editor")
    EditorProfile dtoToEntity(EditorProfileDTO dto, User editor);
}
