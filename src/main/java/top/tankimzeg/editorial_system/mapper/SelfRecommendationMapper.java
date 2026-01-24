package top.tankimzeg.editorial_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.tankimzeg.editorial_system.dto.response.SelfRecommendationVO;
import top.tankimzeg.editorial_system.entity.SelfRecommendation;

/**
 * @author Kim
 * @date 2026/1/23 16:22
 * @description 自荐记录映射器
 */
@Mapper
public interface SelfRecommendationMapper {
    SelfRecommendationMapper INSTANCE = Mappers.getMapper(SelfRecommendationMapper.class);

    @Mapping(target = "applicantName", expression =
    "java(entity.getApplicant().getRealName())")
    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    @Mapping(target = "approverName", expression =
    "java(entity.getApprover().getRealName())")
    @Mapping(target = "requestedAt", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "processedAt", dateFormat = "yyyy-MM-dd")
    SelfRecommendationVO entityToVO(SelfRecommendation entity);
}
