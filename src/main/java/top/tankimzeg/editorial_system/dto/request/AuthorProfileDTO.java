package top.tankimzeg.editorial_system.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "个人资料不能为空")
    @Valid
    private Profile baseProfile;

}
