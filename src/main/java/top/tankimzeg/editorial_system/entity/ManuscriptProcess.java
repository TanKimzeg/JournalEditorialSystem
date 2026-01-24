package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @author Kim
 * @date 2026/1/19 10:47
 * @description 稿件处理流程实体类
 */
@Entity
@Table(name = "manuscript_processes")
@Data
public class ManuscriptProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manuscript_id", nullable = false)
    private Manuscript manuscript;          // 所属稿件

    @Enumerated(EnumType.STRING)
    private Stage stage;                    // 稿件处理阶段

    public enum Stage {
        INITIAL_REVIEW,                     // 初审
        PEER_REVIEW,                        // 同行评审
        EDITORIAL_REVIEW,                   // 编辑评审
        REVISION,                           // 修改阶段
        FINAL_DECISION                      // 最终决定
    }

    @CreationTimestamp
    @Column(name = "processed_at", updatable = false)
    private LocalDateTime processedAt;      // 处理时间

    @UpdateTimestamp
    @Column(name = "finished_at")
    private LocalDateTime finishedAt;       // 完成时间

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "processed_by_user_id", nullable = false)
    private User processedBy;      // 处理人（编辑或审稿人，对应用户）

    private String comments;                // 备注（处理意见、审稿意见、修改说明）
}
