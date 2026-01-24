package top.tankimzeg.editorial_system.dto.request;

import lombok.Getter;
import lombok.Setter;
import top.tankimzeg.editorial_system.entity.Profile;

/**
 * @author Kim
 * @date 2026/1/22 11:23
 * @description 作者个人资料 DTO
 */
@Getter
@Setter
public class AuthorProfileDTO {

    private Profile baseProfile;

}
