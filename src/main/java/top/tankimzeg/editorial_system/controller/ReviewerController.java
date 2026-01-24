package top.tankimzeg.editorial_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import top.tankimzeg.editorial_system.dto.request.ReviewDTO;
import top.tankimzeg.editorial_system.dto.response.ManuscriptProcessVO;
import top.tankimzeg.editorial_system.entity.AuthorProfile;
import top.tankimzeg.editorial_system.entity.ManuscriptProcess;
import top.tankimzeg.editorial_system.entity.Review;
import top.tankimzeg.editorial_system.exception.BusinessException;
import top.tankimzeg.editorial_system.mapper.ReviewRecordMapper;
import top.tankimzeg.editorial_system.repository.AuthorProfileRepo;
import top.tankimzeg.editorial_system.service.*;
import top.tankimzeg.editorial_system.utils.SecurityUtil;
import top.tankimzeg.editorial_system.utils.ApiResponse;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/22 10:49
 * @description 专家审稿控制器
 */
@RestController
@PreAuthorize("hasRole('REVIEWER')")
@RequestMapping("/api/review")
@Tag(name = "评审管理", description = "审稿专家评审相关接口")
public class ReviewerController {

    @Autowired
    private UserService userService;

    @Autowired
    private ManuscriptService manuscriptService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AuthorProfileRepo authorProfileRepo;

    private static final ReviewRecordMapper reviewMapper = ReviewRecordMapper.INSTANCE;

    @Operation(summary = "提交评审", description = "审稿人提交稿件评审意见")
    @PostMapping(value = "/{manuscriptId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Review> submitReviewerReview(
            @PathVariable Long manuscriptId,
            @RequestPart(name = "metadata") ReviewDTO reviewDTO,
            @RequestPart(name = "files", required = false) List<MultipartFile> attachmentFiles
    ) {
        if (!SecurityUtil.isReviewer()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册审稿人，无法提交评审意见");
        }
        ManuscriptProcess latestProcess =
                manuscriptService.getLatestManuscriptProcess(manuscriptId);
        if (!latestProcess.getProcessedBy().equals(
                userService.getUserById(SecurityUtil.getCurrentUserId()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.FORBIDDEN)
                )
        )) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是该稿件的指定审稿人，无法提交评审意见");
        }
        Review review = reviewMapper.dtoToEntity(reviewDTO);
        AuthorProfile reviewer = authorProfileRepo.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "审稿人资料不存在"));
        review.setReviewer(reviewer);
        Review savedReview = reviewService.finishedReview(latestProcess.getId(), review, attachmentFiles);
        return ApiResponse.success(savedReview);
    }

    @Operation(summary = "审稿人查询待审稿件", description = "审稿人查询分配给自己的待审稿件列表")
    @GetMapping("/assignedManuscripts")
    public ApiResponse<List<ManuscriptProcessVO>> getReviewerAssignedManuscripts() {
        if (!SecurityUtil.isReviewer()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "您不是注册审稿人，无法查看待审稿件");
        }
        Long reviewerId = SecurityUtil.getCurrentUserId();
        return ApiResponse.success(reviewService.getReviewTasksByReviewerId(reviewerId));
    }
}
