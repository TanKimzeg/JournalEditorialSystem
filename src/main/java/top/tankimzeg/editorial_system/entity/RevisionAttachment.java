package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/23 12:28
 * @description 修订附件实体类
 */
@Entity
@Table(name = "revision_attachments")
@Data
public class RevisionAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revision_id", nullable = false)
    private Revision revision;

    @Embedded
    private Attachment attachment;
}
