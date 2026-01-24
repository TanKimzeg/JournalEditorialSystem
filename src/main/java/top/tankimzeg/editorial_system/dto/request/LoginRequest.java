package top.tankimzeg.editorial_system.dto.request;

import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/20 21:03
 * @description 登录请求 DTO
 */
@Data
public class LoginRequest {

    private String username;

    private String password;


}
