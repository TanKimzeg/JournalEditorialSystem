package top.tankimzeg.editorial_system.dto.request;

import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/20 21:04
 * @description 注册请求 DTO
 */
@Data
public class RegisterRequest {

    private String username;

    private String password;

    private String email;

    private String realName;

    private String phoneNumber;

    private String role = "AUTHOR";
}
