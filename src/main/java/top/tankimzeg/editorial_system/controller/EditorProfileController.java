package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.tankimzeg.editorial_system.dto.request.EditorProfileDTO;
import top.tankimzeg.editorial_system.dto.response.EditorProfileVO;
import top.tankimzeg.editorial_system.service.EditorProfileService;
import top.tankimzeg.editorial_system.utils.SecurityUtil;
import top.tankimzeg.editorial_system.utils.ApiResponse;

/**
 * @author Kim
 * @date 2026/1/21 10:29
 * @description 编辑个人资料管理控制器
 */
@RestController
@RequestMapping("/api/editorProfile")
@PreAuthorize("hasRole('EDITOR')")
@Tag(name = "编辑资料管理", description = "编辑个人资料的创建、获取、更新与删除接口")
public class EditorProfileController {
    @Autowired
    private EditorProfileService editorProfileService;

    /**
     * 创建用户信息，编辑注册后调用此接口完善个人资料
     *
     * @param profileDTO 编辑个人资料
     * @return 创建的用户信息
     */
    @PostMapping()
    @Operation(summary = "创建编辑信息", description = "创建一个新的编辑信息")
    public ApiResponse<EditorProfileVO> createProfile(@Valid @RequestBody EditorProfileDTO profileDTO) {
        return ApiResponse.success(editorProfileService.createEditorProfile(
                SecurityUtil.getCurrentUserId(), profileDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取编辑信息", description = "根据用户ID获取用户的详细信息")
    public ApiResponse<EditorProfileVO> getProfileById(@PathVariable Long id) {
        return ApiResponse.success(editorProfileService.getEditorProfileById(id));
    }

    @PatchMapping("/me")
    @Operation(summary = "更新编辑信息", description = "根据用户ID更新编辑的详细信息")
    public ApiResponse<EditorProfileVO> updateProfile(@Valid @RequestBody EditorProfileDTO profileDTO) {
        return ApiResponse.success(editorProfileService.patchEditorProfile(
                SecurityUtil.getCurrentUserId(), profileDTO));
    }

    @DeleteMapping("/me")
    @Operation(summary = "删除编辑", description = "根据用户ID删除编辑信息")
    public ApiResponse<Boolean> deleteAuthor() {
        Long id = SecurityUtil.getCurrentUserId();
        editorProfileService.deleteEditorProfile(id);
        return ApiResponse.success(Boolean.TRUE);
    }
}
