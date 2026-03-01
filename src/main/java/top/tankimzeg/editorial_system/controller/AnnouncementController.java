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
@PreAuthorize("hasRole('ADMIN')")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Operation(summary = "新增公告")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AnnouncementVO> create(@Valid @RequestBody AnnouncementRequest request) {
        return ApiResponse.success(announcementService.create(request));
    }

    @Operation(summary = "更新公告")
    @PutMapping("/{id}")
    public ApiResponse<AnnouncementVO> update(
            @PathVariable Long id,
            @Valid @RequestBody AnnouncementRequest request
    ) {
        return ApiResponse.success(announcementService.update(id, request));
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return ApiResponse.success(Boolean.TRUE);
    }

    @Operation(summary = "后台分页查询全部公告")
    @GetMapping("/managed")
    public ApiResponse<Page<AnnouncementVO>> listForEditors(Pageable pageable) {
        return ApiResponse.success(announcementService.listForEditors(pageable));
    }

    @Operation(summary = "获取后台公告详情")
    @GetMapping("/{id}")
    public ApiResponse<AnnouncementVO> getManagedOne(@PathVariable Long id) {
        return ApiResponse.success(announcementService.getManagedOne(id));
    }

    /**
     * 在前端编辑公告时：
     * <li>上传图片 → 调用已经有的 /api/announcements/upload，返回一个 URL 或 key；</li>
     * <li>编辑器将这个 URL 插入到公告内容中；</li>
     * <li>公共读公告页面时，浏览器会直接向这个 URL 发起 GET 请求。</li>
     *
     * @param picture 前端上传的图片文件
     * @return 图片的访问 URL，前端将其插入到公告内容中
     */
    @Operation(summary = "上传公告图片，返回图床URL")
    @PostMapping("/upload")
    public ApiResponse<String> uploadPicture(@RequestParam MultipartFile picture) {
        return ApiResponse.success(announcementService.uploadPicture(picture));
    }

}
