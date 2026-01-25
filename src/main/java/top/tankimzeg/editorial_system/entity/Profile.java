package top.tankimzeg.editorial_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Size(max = 32, message = "证件号长度不能超过32个字符")
    private String identityNumber;  // 身份证号

    @Size(max = 255, message = "地址长度不能超过255个字符")
    private String address;         // 通信地址

    @Size(max = 10, message = "邮编长度不能超过10个字符")
    private String zipcode;         // 邮件编码

    @Size(max = 20, message = "电话长度不能超过20个字符")
    private String phone;           // 联系电话

    @Size(max = 500, message = "研究兴趣长度不能超过500个字符")
    private String interests;       // 研究兴趣

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
