package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kim
 * @date 2026/1/19 11:37
 * @description 修改稿件记录实体类
 */
@Entity
@Table(name = "revisions")
@Data
public class Revision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "process_id", nullable = false)
    private ManuscriptProcess process;          // 所属稿件处理过程

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;                        // 修改稿作者

    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RevisionAttachment> attachments = new HashSet<>();

    private String comments;                    // 修改说明

    @CreationTimestamp
    @Column(name = "started_at", updatable = false)
    private LocalDateTime startedAt;        // 开始修改时间

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;       // 完成修改时间
}
