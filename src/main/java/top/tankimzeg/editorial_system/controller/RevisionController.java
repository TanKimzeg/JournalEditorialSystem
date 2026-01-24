package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.tankimzeg.editorial_system.dto.request.RevisionDTO;
import top.tankimzeg.editorial_system.dto.response.ManuscriptProcessVO;
import top.tankimzeg.editorial_system.entity.ManuscriptProcess;
import top.tankimzeg.editorial_system.entity.Revision;
import top.tankimzeg.editorial_system.exception.BusinessException;
import top.tankimzeg.editorial_system.mapper.RevisionRecordMapper;
import top.tankimzeg.editorial_system.service.ManuscriptService;
import top.tankimzeg.editorial_system.service.RevisionService;
import top.tankimzeg.editorial_system.service.UserService;
import top.tankimzeg.editorial_system.utils.SecurityUtil;
import top.tankimzeg.editorial_system.utils.ApiResponse;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/21 11:17
 * @description 作者修订控制器
 */
@RestController
@PreAuthorize("hasRole('AUTHOR')")
@RequestMapping("/api/revisions")
@Tag(name = "修订管理", description = "作者修订稿件的接口")
public class RevisionController {

    @Autowired
    private RevisionService revisionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ManuscriptService manuscriptService;

    private static final RevisionRecordMapper revisionMapper = RevisionRecordMapper.INSTANCE;

    @Operation(summary = "提交修订", description = "作者提交修订")
    @PostMapping(value = "/{manuscriptId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Revision> submitRevision(
            @PathVariable Long manuscriptId,
            @RequestPart(name = "metadata") RevisionDTO revisionDTO,
            @RequestPart(name = "files", required = false) List<MultipartFile> attachmentFiles
    ) {
        if (!manuscriptService.getManuscriptById(manuscriptId).getAuthor().getId()
                .equals(SecurityUtil.getCurrentUserId())) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您无权提交此稿件的修订");
        }
        Revision revision = revisionMapper.dtoToEntity(revisionDTO);
        revision.setAuthor(userService.getUserById(SecurityUtil.getCurrentUserId()).orElseThrow(
                () -> new BusinessException(HttpStatus.BAD_REQUEST, "您不是注册作者，无法提交修订"))
        );
        ManuscriptProcess latestProcess =
                manuscriptService.getLatestManuscriptProcess(manuscriptId);
        if (!latestProcess.getStage().equals(ManuscriptProcess.Stage.REVISION)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "当前稿件不处于修订阶段，无法提交修订");
        }
        Revision savedRevision = revisionService.finishedRevision(latestProcess.getId(), revision, attachmentFiles);
        return ApiResponse.success(savedRevision);
    }

    @Operation(summary = "作者查询待修订稿件", description = "作者查询自己所有待修订的稿件")
    @GetMapping("/pendingTasks")
    public ApiResponse<List<ManuscriptProcessVO>> getPendingRevisions() {
        if (!SecurityUtil.isAuthor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册作者，无法查询待修订稿件");
        }
        return ApiResponse.success(
                revisionService.getPendingRevisionsByAuthorId(SecurityUtil.getCurrentUserId())
        );
    }
}
