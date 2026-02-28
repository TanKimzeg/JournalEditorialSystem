package top.tankimzeg.editorial_system.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.tankimzeg.editorial_system.dto.request.AnnouncementRequest;
import top.tankimzeg.editorial_system.dto.response.AnnouncementVO;
import top.tankimzeg.editorial_system.entity.Announcement;

@Mapper
public interface AnnouncementMapper {

    AnnouncementMapper INSTANCE = Mappers.getMapper(AnnouncementMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Announcement requestToEntity(AnnouncementRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(AnnouncementRequest request, @MappingTarget Announcement entity);

    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    AnnouncementVO entityToVO(Announcement entity);
}

