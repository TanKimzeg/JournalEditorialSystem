package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.tankimzeg.editorial_system.dto.request.AnnouncementRequest;
import top.tankimzeg.editorial_system.dto.response.AnnouncementVO;
import top.tankimzeg.editorial_system.service.AnnouncementService;
import top.tankimzeg.editorial_system.utils.ApiResponse;

@RestController
@RequestMapping("/api/announcements")
@Tag(name = "公告管理", description = "编辑部公告 CRUD 与发布接口")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Operation(summary = "新增公告")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AnnouncementVO> create(@Valid @RequestBody AnnouncementRequest request) {
        return ApiResponse.success(announcementService.create(request));
    }

    @Operation(summary = "更新公告")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AnnouncementVO> update(
            @PathVariable Long id,
            @Valid @RequestBody AnnouncementRequest request
    ) {
        return ApiResponse.success(announcementService.update(id, request));
    }

    @Operation(summary = "调整公告发布状态")
    @PutMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AnnouncementVO> updatePublish(
            @PathVariable Long id,
            @Valid @RequestBody Boolean request
    ) {
        return ApiResponse.success(announcementService.updatePublishStatus(id, request));
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return ApiResponse.success(Boolean.TRUE);
    }

    @Operation(summary = "后台分页查询全部公告")
    @GetMapping("/managed")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<AnnouncementVO>> listForEditors(Pageable pageable) {
        return ApiResponse.success(announcementService.listForEditors(pageable));
    }

    @Operation(summary = "获取后台公告详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AnnouncementVO> getManagedOne(@PathVariable Long id) {
        return ApiResponse.success(announcementService.getManagedOne(id));
    }

    @Operation(summary = "上传公告图片，返回图床URL")
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> uploadPicture(@RequestParam MultipartFile picture){
        return ApiResponse.success(announcementService.uploadPicture(picture));
    }

    @Operation(summary = "获取单条公告详情")
    @GetMapping("/public/{id}")
    public ApiResponse<AnnouncementVO> getOne(@PathVariable Long id) {
        return ApiResponse.success(announcementService.getPublishedOne(id));
    }

    @Operation(summary = "公开分页查询公告")
    @GetMapping("/public")
    public ApiResponse<Page<AnnouncementVO>> listPublished(Pageable pageable) {
        return ApiResponse.success(announcementService.listPublished(pageable));
    }
}
