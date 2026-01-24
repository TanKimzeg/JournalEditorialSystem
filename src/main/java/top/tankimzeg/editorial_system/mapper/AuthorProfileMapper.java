package top.tankimzeg.editorial_system.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.tankimzeg.editorial_system.dto.request.AuthorProfileDTO;
import top.tankimzeg.editorial_system.dto.response.AuthorProfileVO;
import top.tankimzeg.editorial_system.entity.AuthorProfile;
import top.tankimzeg.editorial_system.entity.User;

/**
 * @author Kim
 * @date 2026/1/23 17:48
 * @description 作者档案映射器
 */
@Mapper
public interface AuthorProfileMapper {

    AuthorProfileMapper INSTANCE = Mappers.getMapper(AuthorProfileMapper.class);

    @Mapping(target = "realName", source = "author.realName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "role", expression = "java(entity.getAuthor().getRole().name())")
    @Mapping(target = "baseProfile", source = "profile")
    AuthorProfileVO entityToVO(AuthorProfile entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "profile", source = "dto.baseProfile")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateEntityFromDTO(AuthorProfileDTO dto, @MappingTarget AuthorProfile entity);

    @Mapping(target = "profile", source = "dto.baseProfile")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "author")
    AuthorProfile dtoToEntity(AuthorProfileDTO dto, User author);
}
