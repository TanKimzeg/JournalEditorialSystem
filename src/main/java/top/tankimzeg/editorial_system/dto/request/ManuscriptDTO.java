package top.tankimzeg.editorial_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Kim
 * @date 2026/1/22 20:51
 * @description 稿件 DTO
 */
@Getter
@Setter
public class ManuscriptDTO {

    @NotBlank(message = "标题不能为空")
    @Size(min = 5, max = 200, message = "标题长度应在5到200个字符之间")
    private String title;

    @NotBlank(message = "摘要不能为空")
    @Size(min = 20, max = 2000, message = "摘要长度应在20到2000个字符之间")
    private String abstractText;

    @NotBlank(message = "关键词不能为空")
    @Size(max = 500, message = "关键词长度不能超过500个字符")
    private String keywords;

    @NotBlank(message = "学科领域不能为空")
    @Size(max = 500, message = "学科领域长度不能超过500个字符")
    private String fields;

}
