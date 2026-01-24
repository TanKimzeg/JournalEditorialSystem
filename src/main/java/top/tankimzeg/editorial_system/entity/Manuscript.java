package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kim
 * @date 2026/1/19 10:32
 * @description 稿件实体类
 */
@Entity
@Table(name = "manuscripts")
@Data
public class Manuscript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String abstractText;

    private String keywords;

    private String fields;  // 研究领域

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;  // 投稿作者

    @Enumerated(EnumType.STRING)
    private ManuscriptStatus status;  // 稿件状态

    public enum ManuscriptStatus {
        SUBMITTED,              // 已提交
        UNDER_REVIEW,           // 审核中
        REVIEW_COMPLETED,       // 审核完成
        UNDER_REVISION,         // 修订中
        REVISION_COMPLETED,     // 修订完成
        ACCEPTED,               // 已接受
        REJECTED,               // 已拒绝
        PUBLISHED,              // 已发表
        WITHDRAWN               // 已撤回
    }

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private LocalDateTime submittedAt;      // 提交时间

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "manuscript", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ManuscriptProcess> manuscriptProcesses = new HashSet<>();

    @OneToMany(mappedBy = "manuscript", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ManuscriptAttachment> attachments = new HashSet<>();
}
