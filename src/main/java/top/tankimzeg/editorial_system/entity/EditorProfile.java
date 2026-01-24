package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/18 23:31
 * @description 编辑个人详细信息实体类
 */
@Entity
@Table(name = "editor_profiles")
@Data
public class EditorProfile {

    @Id
    @Column(name = "editor_id")
    private Long id; // 与 users.id 共享主键（同时作为外键）

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "editor_id", referencedColumnName = "id")
    private User editor;          // 关联的编辑用户

    @Embedded
    private Profile profile;      // 通用个人资料
}
