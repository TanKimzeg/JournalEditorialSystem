package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tankimzeg.editorial_system.dto.response.AnnouncementVO;
import top.tankimzeg.editorial_system.exception.BusinessException;
import top.tankimzeg.editorial_system.service.AnnouncementService;
import top.tankimzeg.editorial_system.service.impl.MediaStorageService;
import top.tankimzeg.editorial_system.utils.ApiResponse;

import java.io.IOException;

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

    @Autowired
    private MediaStorageService mediaStorageService;

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

    @Operation(summary = "按存储 key 公开查看公告图片", description = "浏览器直接访问该地址即可内联查看图片")
    @GetMapping("/media/{key}")
    public void viewMedia(@PathVariable("key") String storageKey, HttpServletResponse response) {
        try {
            byte[] data = mediaStorageService.load(storageKey);
            if (data == null || data.length == 0) {
                throw new BusinessException(org.springframework.http.HttpStatus.NOT_FOUND, "图片不存在");
            }
            // 目前无法从 key 推断原始类型，这里使用通用图片类型；
            // 如果将来在 MediaStorageService 中保存并查询 contentType，可在此处替换为真实类型。
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
            response.getOutputStream().write(data);
            response.flushBuffer();
        } catch (IOException e) {
            throw new BusinessException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "图片读取失败，请稍后重试");
        }
    }
}
