package top.tankimzeg.editorial_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/23 18:57
 * @description 评审提交请求
 */
@Data
public class ReviewDTO {

    @NotBlank(message = "评审决策不能为空")
    @Pattern(regexp = "^(ACCEPT|REJECT|REVISION)$", message = "评审决策只能是ACCEPT、REJECT或REVISION")
    private String decision;

    @NotBlank(message = "评审意见不能为空")
    @Size(min = 10, max = 5000, message = "评审意见长度应在10到5000个字符之间")
    private String comments;
}
