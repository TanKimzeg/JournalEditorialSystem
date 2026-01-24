package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * @author Kim
 * @date 2026/1/23 12:30
 * @description 附件基类
 */
@Embeddable
@Data
public class Attachment {
    @Column(nullable = false)
    private String filename;      // 原始文件名

    @Column(nullable = false)
    private String contentType;   // MIME 类型

    @Column(nullable = false)
    private String storagePath;   // 服务器存储路径

    private Long size;            // 字节大小

    @CreationTimestamp
    private LocalDateTime uploadedAt; // 上传时间
}
