package top.tankimzeg.editorial_system.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 公告对外展示视图
 */
@Getter
@Setter
public class AnnouncementVO {

    private Long id;

    private String title;

    private String content;

    /**
     * 公告摘要
     */
    private String summary;

    /**
     * 是否置顶
     */
    private Boolean pinned;

    private String status;

    private LocalDateTime publishedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
