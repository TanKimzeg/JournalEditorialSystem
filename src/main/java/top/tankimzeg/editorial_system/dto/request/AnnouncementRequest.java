package top.tankimzeg.editorial_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementRequest {

    @NotBlank(message = "公告标题不能为空")
    @Size(max = 150, message = "公告标题长度不能超过150个字符")
    private String title;

    /**
     * 公告内容，支持 Markdown 或 HTML
     */
    @NotBlank(message = "公告内容不能为空")
    private String content;

    /**
     * 简要摘要，用于列表页展示
     */
    @Size(max = 300, message = "公告摘要长度不能超过300个字符")
    private String summary;

    /**
     * 是否置顶
     */
    private Boolean pinned = Boolean.FALSE;

    /**
     * 是否发布（立即生效），true=发布，false=保存为草稿
     */
    @NotNull(message = "请明确公告是否发布")
    private Boolean publish;
}
