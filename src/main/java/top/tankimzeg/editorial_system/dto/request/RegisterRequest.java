package top.tankimzeg.editorial_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/20 21:04
 * @description 注册请求 DTO
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度应在3到20个字符之间")
    @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 64, message = "密码长度应在8到64个字符之间")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, max = 50, message = "真实姓名长度应在2到50个字符之间")
    private String realName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^\\+?[0-9\\-]{7,20}$", message = "手机号格式不正确")
    private String phoneNumber;

    private String role = "AUTHOR";
}
