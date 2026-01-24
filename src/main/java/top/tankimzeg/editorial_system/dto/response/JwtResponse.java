package top.tankimzeg.editorial_system.dto.response;

import lombok.Data;

/**
 * @author Kim
 * @date 2026/1/20 21:06
 * @description JWT 响应 DTO
 */
@Data
public class JwtResponse {

    private String token;

    private String type = "Bearer";

    private Long id;

    private String username;

    private String realName;

    private String email;

    public JwtResponse(
            String accessToken,
            Long id,
            String username,
            String realName,
            String email
    ) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.email = email;
    }

}
