package top.tankimzeg.editorial_system.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kim
 * @date 2026/1/23 16:19
 * @description 自荐信息 VO
 */
@Getter
@Setter
public class SelfRecommendationVO {
    private String applicantName;

    private String status;
    private String approverName;
    private String requestedAt;
    private String processedAt;
    private String applyReason;
    private String comments;
}
