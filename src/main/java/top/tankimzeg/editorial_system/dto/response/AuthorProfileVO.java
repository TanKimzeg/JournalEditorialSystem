package top.tankimzeg.editorial_system.dto.response;

import lombok.Getter;
import lombok.Setter;
import top.tankimzeg.editorial_system.entity.Profile;


/**
 * @author Kim
 * @date 2026/1/22 16:39
 * @description 作者个人资料 VO
 */
@Getter
@Setter
public class AuthorProfileVO {

    private String realName;

    private String email;

    private String role;

    private Profile baseProfile;


}
