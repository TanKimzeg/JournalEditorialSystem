package top.tankimzeg.editorial_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/21 11:58
 * @description 编辑决策 DTO
 */
@Data
public class EditorDecisionDTO {

    @NotBlank(message = "决策不能为空")
    @Pattern(regexp = "^(ACCEPT|REJECT|REVISION)$", message = "决策只能是ACCEPT、REJECT或REVISION")
    private String decision; // "ACCEPT", "REJECT", "REVISE"

    @NotBlank(message = "评论不能为空")
    @Size(min = 5, max = 2000, message = "评论长度应在5到2000个字符之间")
    private String comments; // 主编的评审意见
}
