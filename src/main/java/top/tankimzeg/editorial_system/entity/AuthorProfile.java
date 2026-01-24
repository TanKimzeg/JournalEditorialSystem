package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/18 23:21
 * @description 作者、专家个人详细信息实体类
 */
@Entity
@Table(name = "author_profiles")
@Data
public class AuthorProfile {

    @Id
    @Column(name = "author_id")
    private Long id; // 与 users.id 共享主键（同时作为外键）

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;          // 关联的作者用户

    @Embedded
    private Profile profile;      // 通用个人资料
}
