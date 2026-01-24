package top.tankimzeg.editorial_system.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kim
 * @date 2026/1/23 18:49
 * @description 评审记录视图
 */
@Data
public class ReviewVO {
    private Long id;

    private Long processId;
    private Long manuscriptId;
    private String manuscriptTitle;

    private Long reviewerId;
    private String reviewerName;

    private String decision;

    private String comments;

    private LocalDateTime reviewedAt;      // 审稿时间
    private LocalDateTime finishedAt;       // 完成时间

    private List<String> attachmentIds;
}
