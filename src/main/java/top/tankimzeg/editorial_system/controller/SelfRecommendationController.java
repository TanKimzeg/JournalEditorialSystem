package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import top.tankimzeg.editorial_system.dto.request.ApproveSelfRecommendationDTO;
import top.tankimzeg.editorial_system.dto.response.SelfRecommendationVO;
import top.tankimzeg.editorial_system.entity.User;
import top.tankimzeg.editorial_system.exception.BusinessException;
import top.tankimzeg.editorial_system.service.AuthorProfileService;
import top.tankimzeg.editorial_system.service.SelfRecommendationService;
import top.tankimzeg.editorial_system.service.UserService;
import top.tankimzeg.editorial_system.utils.ApiResponse;
import top.tankimzeg.editorial_system.utils.SecurityUtil;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/21 12:36
 * @description 自荐管理控制器
 */
@RestController
@RequestMapping("/api/selfRecommendation")
@Tag(name = "自荐管理", description = "作者自荐审稿专家的控制器")
public class SelfRecommendationController {

    @Autowired
    private SelfRecommendationService selfRecommendationService;

    @Autowired
    private AuthorProfileService authorProfileService;
    @Autowired
    private UserService userService;

    @Operation(summary = "提交申请", description = "作者提交自荐审稿专家申请")
    @PostMapping("/apply")
    public ApiResponse<SelfRecommendationVO> applySelfRecommendation(
            @Valid @RequestBody String reason) {
        if (!SecurityUtil.getCurrentUserRole()
                .equals(User.Role.AUTHOR)) {
            throw new BusinessException(HttpStatus.FORBIDDEN,
                    "只有作者才能提交自荐审稿专家申请");
        }
        if (SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN,
                    "编辑不能提交自荐审稿专家申请");
        }
        if (SecurityUtil.isReviewer()) {
            throw new BusinessException(HttpStatus.FORBIDDEN,
                    "您已经是审稿专家，不能提交自荐审稿专家申请");
        }
        User applicant = userService
                .getUserById(SecurityUtil.getCurrentUserId()).orElseThrow(
                        () -> new BusinessException(HttpStatus.BAD_REQUEST, "用户不存在"));
        return ApiResponse.success(selfRecommendationService.apply(applicant, reason));
    }

    @Operation(summary = "主编审批", description = "主编审批作者自荐审稿专家申请")
    @PostMapping("/approve/{id}")
    public ApiResponse<SelfRecommendationVO> approveSelfRecommendation(
            @PathVariable Long id, @Valid @RequestBody ApproveSelfRecommendationDTO approveDto
    ) {
        if (!SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN,
                    "只有主编才能审批作者自荐审稿专家申请");
        }
        return ApiResponse.success(selfRecommendationService.approve(id,
                approveDto.getApproved(), approveDto.getComments())
        );
    }

    @Operation(summary = "主编查询申请", description = "主编查询自己待审批的自荐审稿专家申请")
    @GetMapping("/pendingTasks")
    public ApiResponse<List<SelfRecommendationVO>> getPendingTasks() {
        if (!SecurityUtil.isEditor()) {
            throw new BusinessException(HttpStatus.FORBIDDEN,
                    "只有主编才能查询待审批的自荐审稿专家申请");
        }
        return ApiResponse.success(
                selfRecommendationService.getPendingTasks(SecurityUtil.getCurrentUserId())
        );
    }

    @Operation(summary = "作者查询申请状态", description = "作者查询自己的自荐审稿专家申请状态")
    @GetMapping("/myApplication")
    public ApiResponse<List<SelfRecommendationVO>> getMyApplication() {
        if (!SecurityUtil.isAuthor()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "只有作者才能查询自己的自荐审稿专家申请状态");
        }

        return ApiResponse.success(
                selfRecommendationService.getAuthorApplications(SecurityUtil.getCurrentUserId())
        );
    }


}
