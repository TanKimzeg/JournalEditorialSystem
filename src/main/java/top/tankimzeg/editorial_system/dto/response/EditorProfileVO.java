package top.tankimzeg.editorial_system.dto.response;

import lombok.Getter;
import lombok.Setter;
import top.tankimzeg.editorial_system.entity.Profile;

/**
 * @author Kim
 * @date 2026/1/23 18:12
 * @description 编辑个人资料 VO
 */
@Getter
@Setter
public class EditorProfileVO {

    private String realName;

    private String email;

    private String role;

    private Profile baseProfile;
}
