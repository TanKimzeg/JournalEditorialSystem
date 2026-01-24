package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kim
 * @date 2026/1/19 11:06
 * @description 审稿意见实体类
 */
@Entity
@Table(name = "reviews")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id", nullable = false)
    private ManuscriptProcess process;      // 所属稿件处理过程

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private AuthorProfile reviewer;        // 审稿人

    @Enumerated(EnumType.STRING)
    private ReviewDecision decision;      // 审稿决定

    public enum ReviewDecision {
        ACCEPT,                         // 接受
        MINOR_REVISION,                 // 小修
        MAJOR_REVISION,                 // 大修
        REJECT                          // 拒绝
    }

    @CreationTimestamp
    @Column(name = "reviewed_at", updatable = false)
    private LocalDateTime reviewedAt;      // 审稿时间

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;       // 完成时间

    private String comments;                // 审稿意见

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReviewAttachment> attachments = new HashSet<>();
}
