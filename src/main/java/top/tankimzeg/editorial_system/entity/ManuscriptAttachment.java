package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 文件附件实体，关联稿件
 */
@Entity
@Table(name = "manuscript_attachments")
@Data
public class ManuscriptAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manuscript_id", nullable = false)
    private Manuscript manuscript;

    @Embedded
    private Attachment attachment;
}
