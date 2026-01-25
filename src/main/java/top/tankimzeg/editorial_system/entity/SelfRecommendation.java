package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * @author Kim
 * @date 2026/1/18 23:17
 * @description 自荐审稿专家的记录的实体类
 */
@Entity
@Table(name = "reviewer_recommendations")
@Data
public class SelfRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User applicant;    // 申请人

    @Enumerated(EnumType.STRING)
    private Status status;                  // 审核状态

    public enum Status {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor_id")
    private User approver;    // 审核编辑

    @CreationTimestamp
    @Column(name = "requested_at", updatable = false)
    private LocalDateTime requestedAt;      // 申请时间

    private LocalDateTime processedAt;      // 处理时间

    private String applyReason;            // 申请理由

    private String comments;                  // 审核意见
}
