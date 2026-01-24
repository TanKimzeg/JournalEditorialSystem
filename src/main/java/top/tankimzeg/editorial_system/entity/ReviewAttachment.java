package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/23 12:31
 * @description 评审附件实体类
 */
@Entity
@Table(name = "review_attachments")
@Data
public class ReviewAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Embedded
    private Attachment attachment;
}
