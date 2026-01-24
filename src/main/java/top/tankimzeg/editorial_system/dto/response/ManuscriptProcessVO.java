package top.tankimzeg.editorial_system.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/23 15:35
 * @description 稿件处理流程 VO
 */
@Getter
@Setter
public class ManuscriptProcessVO {
    private Long id;
    private String stage;
    private String processedAt;
    private String finishedAt;
    private String processedByRole;
    private String comments;

    private List<String> attachments;

}
