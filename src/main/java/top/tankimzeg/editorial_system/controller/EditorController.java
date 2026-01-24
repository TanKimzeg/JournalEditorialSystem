package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import top.tankimzeg.editorial_system.dto.request.EditorDecisionDTO;
import top.tankimzeg.editorial_system.dto.response.ManuscriptProcessVO;
import top.tankimzeg.editorial_system.entity.Manuscript;
import top.tankimzeg.editorial_system.entity.ManuscriptProcess;
import top.tankimzeg.editorial_system.exception.BusinessException;
import top.tankimzeg.editorial_system.service.ManuscriptProcessService;
import top.tankimzeg.editorial_system.service.ManuscriptService;
import top.tankimzeg.editorial_system.service.ReviewService;
import top.tankimzeg.editorial_system.service.UserService;
import top.tankimzeg.editorial_system.utils.SecurityUtil;
import top.tankimzeg.editorial_system.utils.ApiResponse;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/21 11:47
 * @description 编辑评审管理控制器，未测试
 */
@RestController
@PreAuthorize("hasRole('EDITOR')")
@RequestMapping("/api/editor")
@Tag(name = "评审管理", description = "编辑评审相关接口")
public class EditorController {

    @Autowired
    private UserService userService;

    @Autowired
    private ManuscriptService manuscriptService;

    @Autowired
    private ManuscriptProcessService manuscriptProcessService;

    @Autowired
    private ReviewService reviewService;


    @Operation(summary = "主编最终决策评审", description = "主编根据审稿意见做出最终决策")
    @PostMapping("/finalDecision/{manuscriptId}")
    public ApiResponse<ManuscriptProcessVO> editorFinalDecision(
            @PathVariable Long manuscriptId, @RequestBody EditorDecisionDTO decision
    ) {
        if (!SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册主编，无法进行最终决策");
        }
        ManuscriptProcess latestProcess =
                manuscriptService.getLatestManuscriptProcess(manuscriptId);
        String comment = "主编决策: " + decision.getDecision() + ". 评语: " + decision.getComments();
        Manuscript.ManuscriptStatus finalDecision = Manuscript.ManuscriptStatus.valueOf(
                decision.getDecision().toUpperCase()
        );
        ManuscriptProcessVO decisionProcess = reviewService.finishedFinalDecision(
                latestProcess.getId(), finalDecision, comment
        );
        return ApiResponse.success(decisionProcess);
    }

    @Operation(summary = "编辑稿件初审", description = "编辑对稿件进行初步审核，决定是否送审")
    @PostMapping("/initialReview/{manuscriptId}")
    public ApiResponse<ManuscriptProcessVO> editorInitialReview(
            @PathVariable Long manuscriptId, @RequestBody EditorDecisionDTO decision
    ) {
        if (!SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册编辑，无法进行初审");
        }
        ManuscriptProcess latestProcess =
                manuscriptService.getLatestManuscriptProcess(manuscriptId);
        String comment = "编辑初审决策: " + decision.getDecision() + ". 评语: " + decision.getComments();
        Manuscript.ManuscriptStatus initialDecision = Manuscript.ManuscriptStatus.valueOf(
                decision.getDecision().toUpperCase()
        );
        ManuscriptProcessVO reviewProcess = reviewService.finishedInitialReview(
                latestProcess.getId(), initialDecision, comment
        );
        return ApiResponse.success(reviewProcess);

    }

    @Operation(summary = "编辑送审", description = "编辑将稿件送审给指定审稿人")
    @PostMapping("/send2Reviewer/{manuscriptId}")
    public ApiResponse<ManuscriptProcess> editorSend2Reviewer(
            @PathVariable Long manuscriptId, @RequestParam Long reviewerId
    ) {
        if (!SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册编辑，无法送审");
        }
        ManuscriptProcess reviewProcess = manuscriptProcessService.addReviewProcess(
                userService.getUserById(reviewerId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
                ), manuscriptId
        );
        return ApiResponse.success(reviewProcess);
    }

    @Operation(summary = "编辑送修订", description = "编辑将稿件送回作者进行修订")
    @PostMapping("/send2Author/{manuscriptId}")
    public ApiResponse<ManuscriptProcess> editorSend2Author(
            @PathVariable Long manuscriptId, @RequestBody String comment
    ){
        if (!SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册编辑，无法送修订");
        }
        ManuscriptProcess revisionProcess = manuscriptProcessService.addRevisionProcess(
                manuscriptService.getManuscriptById(manuscriptId).getAuthor(),
                manuscriptId,
                comment
        );
        return ApiResponse.success(revisionProcess);
    }

    @Operation(summary = "主编查询待决稿件", description = "主编查询待决策的稿件列表")
    @GetMapping("/pendingManuscripts")
    public ApiResponse<List<ManuscriptProcessVO>> getEditorPendingManuscripts() {
        if (!SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册主编，无法查看待决稿件");
        }
        Long editorId = SecurityUtil.getCurrentUserId();
        return ApiResponse.success(
                reviewService.getFinalDecisionTasksByEditorId(editorId)
        );
    }

    @Operation(summary = "编辑查询待初审稿件", description = "编辑查询待初审的稿件列表")
    @GetMapping("/initialReviewManuscripts")
    public ApiResponse<List<ManuscriptProcess>> getEditorInitialReviewManuscripts() {
        if (!SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册编辑，无法查看待初审稿件");
        }
        Long editorId = SecurityUtil.getCurrentUserId();
        return ApiResponse.success(
                reviewService.getInitialReviewTasksByEditorId(editorId)
        );
    }

    @Operation(summary = "编辑查询待送审稿件", description = "编辑查询待送审的稿件列表")
    @GetMapping("/send2ReviewerManuscripts")
    public ApiResponse<List<ManuscriptProcessVO>> getEditorSend2ReviewerManuscripts() {
        if (!SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册编辑，无法查看待送审稿件");
        }
        Long editorId = SecurityUtil.getCurrentUserId();
        return ApiResponse.success(
                reviewService.getEditorReviewTasksByEditorId(editorId)
        );
    }

}
