package top.tankimzeg.editorial_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/23 18:58
 * @description 提交修订请求
 */
@Data
public class RevisionDTO {

    @NotBlank(message = "修订说明不能为空")
    @Size(min = 10, max = 5000, message = "修订说明长度应在10到5000个字符之间")
    private String comments;
}
