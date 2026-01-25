package top.tankimzeg.editorial_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/21 14:38
 * @description 审批自荐请求 DTO
 */
@Data
public class ApproveSelfRecommendationDTO {

    @NotNull(message = "审批结果不能为空")
    private Boolean approved;

    @NotBlank(message = "审批意见不能为空")
    @Size(min = 5, max = 2000, message = "审批意见长度应在5到2000个字符之间")
    private String comments;
}
