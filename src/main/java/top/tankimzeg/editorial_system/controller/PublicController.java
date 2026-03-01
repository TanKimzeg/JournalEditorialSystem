package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tankimzeg.editorial_system.dto.response.AnnouncementVO;
import top.tankimzeg.editorial_system.service.AnnouncementService;
import top.tankimzeg.editorial_system.utils.ApiResponse;

/**
 * @author Kim
 * @date 2026/3/1 10:57
 * @description 公开接口控制器，提供无需认证即可访问的查询功能
 */
@RestController
@RequestMapping("/api/public")
@Tag(name = "公开接口", description = "无需认证即可访问的查询接口")
public class PublicController {
    @Autowired
    private AnnouncementService announcementService;

    @Operation(summary = "获取单条公告详情")
    @GetMapping("/announcements/{id}")
    public ApiResponse<AnnouncementVO> getOne(@PathVariable Long id) {
        return ApiResponse.success(announcementService.getPublishedOne(id));
    }

    @Operation(summary = "公开分页查询公告")
    @GetMapping("/announcements")
    public ApiResponse<Page<AnnouncementVO>> listPublished(Pageable pageable) {
        return ApiResponse.success(announcementService.listPublished(pageable));
    }
}
