package top.tankimzeg.editorial_system.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kim
 * @date 2026/1/23 18:48
 * @description 修改稿件记录视图
 */
@Data
public class RevisionVO {

    private Long id;
    private Long processId;
    private Long manuscriptId;

    private Long authorId;
    private String authorName;

    private String comments;

    private LocalDateTime startedAt;        // 开始修改时间
    private LocalDateTime finishedAt;       // 完成修改时间
    private List<String> attachmentIds;
}
