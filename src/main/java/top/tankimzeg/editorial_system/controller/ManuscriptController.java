package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.tankimzeg.editorial_system.dto.request.ManuscriptDTO;
import top.tankimzeg.editorial_system.dto.response.ManuscriptProcessVO;
import top.tankimzeg.editorial_system.dto.response.ManuscriptVO;
import top.tankimzeg.editorial_system.entity.ManuscriptAttachment;
import top.tankimzeg.editorial_system.entity.Manuscript;
import top.tankimzeg.editorial_system.entity.User;
import top.tankimzeg.editorial_system.exception.BusinessException;
import top.tankimzeg.editorial_system.service.EditorService;
import top.tankimzeg.editorial_system.service.FileStorageService;
import top.tankimzeg.editorial_system.service.ManuscriptProcessService;
import top.tankimzeg.editorial_system.service.ManuscriptService;
import top.tankimzeg.editorial_system.utils.SecurityUtil;
import top.tankimzeg.editorial_system.utils.ApiResponse;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/20 11:32
 * @description 稿件管理控制器，未测试
 */
@RestController
@RequestMapping("/api/manuscripts")
@Tag(name = "稿件管理", description = "稿件的CRUD接口和状态管理")
public class ManuscriptController {

    @Autowired
    private ManuscriptService manuscriptService;

    @Autowired
    private ManuscriptProcessService manuscriptProcessService;

    @Autowired
    private EditorService editorService;

    @Autowired
    private FileStorageService fileStorageService;


    @Operation(summary = "提交稿件", description = "作者提交新的稿件")
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping(value = "/submit", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<ManuscriptVO> submitManuscript(
            @RequestPart(name = "files") List<MultipartFile> attachments,
            @RequestPart(name = "metadata") ManuscriptDTO manuscriptDTO) {
        Long authorId = SecurityUtil.getCurrentUserId();
        if (!SecurityUtil.isAuthor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册作者，无法提交稿件");
        }
        ManuscriptVO savedManuscriptVO = manuscriptService.submitManuscript(manuscriptDTO, authorId, attachments);
        User assignedEditor = editorService.assignToInitialReview(savedManuscriptVO.getId());
        manuscriptProcessService.addSubmissionProcess(assignedEditor, savedManuscriptVO.getId());
        return ApiResponse.success(savedManuscriptVO);
    }

    @Operation(summary = "更新稿件", description = "作者更新已提交的稿件")
    @PreAuthorize("hasRole('AUTHOR')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ManuscriptVO> updateManuscript(
            @PathVariable Long id,
            @RequestPart(name = "files", required = false) List<MultipartFile> attachments,
            @RequestPart(name = "metadata") ManuscriptDTO manuscriptDTO) {
        Long authorId = SecurityUtil.getCurrentUserId();
        if (!SecurityUtil.isAuthor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册作者，无法更新稿件");
        }
        Manuscript existingManuscript = manuscriptService.getManuscriptById(id);
        if (!existingManuscript.getAuthor().getId().equals(authorId)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您无权更新此稿件");
        }
        ManuscriptVO updatedManuscriptVO = manuscriptService.updateManuscript(id, manuscriptDTO, attachments);
        return ApiResponse.success(updatedManuscriptVO);
    }

    @Operation(summary = "获取作者稿件列表", description = "获取当前登录作者的所有稿件列表")
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/myManuscripts")
    public ApiResponse<List<ManuscriptVO>> getMyManuscripts() {
        if (!SecurityUtil.isAuthor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册作者，无法查看稿件列表");
        }
        Long authorId = SecurityUtil.getCurrentUserId();
        List<ManuscriptVO> manuscripts = manuscriptService.getManuscriptsByAuthorId(authorId);
        return ApiResponse.success(manuscripts);
    }

    @Operation(summary = "获取稿件详情", description = "根据稿件ID获取稿件的详细信息")
    @GetMapping("/{id}")
    public ApiResponse<Manuscript> getManuscriptById(@PathVariable Long id) {
        Manuscript manuscript = manuscriptService.getManuscriptById(id);
        return ApiResponse.success(manuscript);
    }

    @Operation(summary = "撤回稿件", description = "作者撤回已提交的稿件")
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/withdraw/{id}")
    public ApiResponse<ManuscriptVO> withdrawManuscript(@PathVariable Long id) {
        if (!SecurityUtil.isAuthor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册作者，无法撤回稿件");
        }
        Long authorId = SecurityUtil.getCurrentUserId();
        Manuscript manuscript = manuscriptService.getManuscriptById(id);
        if (!manuscript.getAuthor().getId().equals(authorId)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您无权撤回此稿件");
        }
        return ApiResponse.success(manuscriptService.withdrawManuscript(id));

    }

    @Operation(summary = "查询稿件所有流程", description = "根据稿件ID查询该稿件的所有处理流程")
    @GetMapping("/processes/{id}")
    public ApiResponse<List<ManuscriptProcessVO>> getProcessesByManuscriptId(@PathVariable Long id) {
        Long authorId = SecurityUtil.getCurrentUserId();
        Manuscript manuscript = manuscriptService.getManuscriptById(id);
        if (!manuscript.getAuthor().getId().equals(authorId)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您无权查看此稿件的处理流程");
        }
        return ApiResponse.success(manuscriptService.getManuscriptProcesses(id));
    }

    @Operation(summary = "出版稿件", description = "将已接受的稿件标记为已出版")
    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/publish/{id}")
    public ApiResponse<ManuscriptVO> publishManuscript(@PathVariable Long id) {
        if (!SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册编辑，无法出版稿件");
        }
        ManuscriptVO publishedManuscript = manuscriptService.publishManuscript(id);
        return ApiResponse.success(publishedManuscript);
    }

    /**
     * 列出稿件附件
     *
     * @param id 稿件ID
     * @return 附件列表
     */
    @Operation(summary = "列出稿件附件", description = "根据稿件ID列出所有附件ID")
    @GetMapping("/{id}/attachments")
    public ApiResponse<List<String>> listAttachments(@PathVariable Long id) {
        Long authorId = SecurityUtil.getCurrentUserId();
        Manuscript manuscript = manuscriptService.getManuscriptById(id);
        if (!manuscript.getAuthor().getId().equals(authorId)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您无权查看此稿件的附件");
        }
        return ApiResponse.success(manuscriptService.listAttachmentIds(id));
    }

    @Operation(summary = "下载附件", description = "根据附件ID下载文件")
//    @PreAuthorize("hasRole('AUTHOR') or hasRole('EDITOR')")
    @GetMapping("/attachments/{attachmentId}/download")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable String attachmentId) {
        ManuscriptAttachment att = manuscriptService.getAttachment(attachmentId);
        if (SecurityUtil.getCurrentUserRole()
                .equals(User.Role.AUTHOR) &&
                !(att.getManuscript().getAuthor().getId()
                        .equals(SecurityUtil.getCurrentUserId()))) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您无权下载此附件");
        }
        try {
            byte[] data = fileStorageService.load(att.getAttachment().getStoragePath());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + att.getAttachment().getFilename())
                    .contentType(MediaType.parseMediaType(att.getAttachment().getContentType()))
                    .body(data);
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "文件下载失败");
        }
    }
}
