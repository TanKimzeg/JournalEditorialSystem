package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @author Kim
 * @date 2026/1/19 17:33
 * @description 用户个人详细信息实体类（嵌入类）
 */
@Embeddable
@Data
public class Profile {

    private String identityNumber;  // 身份证号

    private String address;         // 通信地址

    private String zipcode;         // 邮件编码

    private String phone;           // 联系电话

    private String interests;       // 研究兴趣

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
