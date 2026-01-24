package top.tankimzeg.editorial_system.dto.request;

import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/21 14:38
 * @description 审批自荐请求 DTO
 */
@Data
public class ApproveSelfRecommendationDTO {

    private Boolean approved;

    private String comments;
}
