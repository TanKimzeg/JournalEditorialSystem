package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.tankimzeg.editorial_system.dto.request.AuthorProfileDTO;
import top.tankimzeg.editorial_system.dto.response.AuthorProfileVO;
import top.tankimzeg.editorial_system.service.AuthorProfileService;
import top.tankimzeg.editorial_system.utils.SecurityUtil;
import top.tankimzeg.editorial_system.utils.ApiResponse;

/**
 * @author Kim
 * @date 2026/1/21 10:29
 * @description 作者个人资料管理控制器
 */
@RestController
@RequestMapping("/api/authorProfile")
@Tag(name = "用户信息管理", description = "作者个人信息的CRUD接口")
public class AuthorProfileController {

    @Autowired
    private AuthorProfileService authorProfileService;

    /**
     * 创建用户信息，作者注册后调用此接口完善个人资料
     *
     * @param profile 作者个人资料
     * @return 创建的用户信息
     */
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping()
    @Operation(summary = "创建用户信息", description = "创建一个新的用户信息")
    public ApiResponse<AuthorProfileVO> createProfile(@RequestBody AuthorProfileDTO profile) {
        return ApiResponse.success(authorProfileService.createAuthorProfile(
                SecurityUtil.getCurrentUserId(), profile));
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/me")
    @Operation(summary = "获取用户信息", description = "获取用户自己的详细信息")
    public ApiResponse<AuthorProfileVO> getMyProfile() {
        Long id = SecurityUtil.getCurrentUserId();
        return ApiResponse.success(authorProfileService.getAuthorProfileById(id));
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PatchMapping("/me")
    @Operation(summary = "更新用户信息", description = "根据用户ID更新用户的详细信息")
    public ApiResponse<AuthorProfileVO> updateProfile(@RequestBody AuthorProfileDTO profile) {
        Long id = SecurityUtil.getCurrentUserId();
        return ApiResponse.success(authorProfileService.patchAuthorProfile(id, profile));
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping("/me")
    @Operation(summary = "删除用户信息", description = "删除用户个人资料")
    public ApiResponse<Boolean> deleteProfile() {
        Long id = SecurityUtil.getCurrentUserId();
        authorProfileService.deleteAuthorProfile(id);
        return ApiResponse.success(Boolean.TRUE);
    }


    @PreAuthorize("hasRole('EDITOR')")
    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息", description = "获取用户作者的详细信息")
    public ApiResponse<AuthorProfileVO> getProfileById(
            @PathVariable Long id
    ) {
        return ApiResponse.success(authorProfileService.getAuthorProfileById(id));
    }

}
