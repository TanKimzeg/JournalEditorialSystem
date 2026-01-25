package top.tankimzeg.editorial_system.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import top.tankimzeg.editorial_system.entity.Profile;

/**
 * @author Kim
 * @date 2026/1/23 18:11
 * @description 编辑个人资料 DTO
 */
@Getter
@Setter
public class EditorProfileDTO {

    @NotNull(message = "个人资料不能为空")
    @Valid
    private Profile baseProfile;
}
