package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.tankimzeg.editorial_system.dto.response.ManuscriptProcessVO;
import top.tankimzeg.editorial_system.entity.Manuscript;
import top.tankimzeg.editorial_system.entity.ManuscriptProcess;
import top.tankimzeg.editorial_system.entity.Review;
import top.tankimzeg.editorial_system.mapper.ManuscriptProcessMapper;
import top.tankimzeg.editorial_system.repository.ManuscriptProcessRepo;
import top.tankimzeg.editorial_system.repository.ManuscriptRepo;
import top.tankimzeg.editorial_system.repository.ReviewRepo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kim
 * @date 2026/1/19 16:15
 * @description 评审服务类
 */
@Service
public class ReviewService {

    @Autowired
    private ManuscriptRepo manuscriptRepo;

    @Autowired
    private ManuscriptProcessRepo manuscriptProcessRepo;

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private FileStorageService fileStorageService;

    private static final ManuscriptProcessMapper processMapper = ManuscriptProcessMapper.INSTANCE;

    /**
     * 完成评审并保存评审意见，审稿人完成评审后调用此方法
     *
     * @param processId 稿件处理流程ID
     * @param review    评审意见
     * @return 更新后的稿件处理流程记录
     */
    @Transactional
    public Review finishedReview(Long processId, Review review, List<MultipartFile> files) {
        // 获取最新一条稿件处理流程记录，即分配评审的记录
        ManuscriptProcess process = manuscriptProcessRepo.getReferenceById(processId);
        if (!process.getStage().equals(ManuscriptProcess.Stage.PEER_REVIEW))
            throw new RuntimeException("当前流程阶段不允许审稿专家完成评审");

        process.setComments(review.getComments());
        manuscriptProcessRepo.save(process);
        // 关联评审记录和稿件处理流程
        review.setProcess(process);
        review.setFinishedAt(LocalDateTime.now());
        Review saved = reviewRepo.save(review);
        try {
            fileStorageService.store(files, saved);
        } catch (IOException e) {
            throw new RuntimeException("保存评审附件失败", e);
        }
        // 更新稿件状态为评审完成
        Manuscript manuscript = process.getManuscript();
        manuscript.setStatus(Manuscript.ManuscriptStatus.REVIEW_COMPLETED);
        // 保存更新后的稿件
        manuscriptRepo.save(manuscript);
        // 保存稿件处理流程记录
        manuscriptProcessRepo.save(process);
        return saved;
    }

    /**
     * 编辑完成最终决定，编辑做出接受或拒绝的最终决定后调用此方法
     *
     * @param processId     稿件处理流程ID
     * @param finalDecision 最终决定状态
     * @param comment       备注
     * @return 更新后的稿件处理流程记录
     */
    @Transactional
    public ManuscriptProcessVO finishedFinalDecision(
            Long processId, Manuscript.ManuscriptStatus finalDecision, String comment) {
        ManuscriptProcess process = manuscriptProcessRepo.getReferenceById(processId);
        if (!process.getStage().equals(ManuscriptProcess.Stage.FINAL_DECISION))
            throw new RuntimeException("当前流程阶段不允许做最终决定");

        switch (finalDecision) {
            case ACCEPTED, REJECTED -> {
                // 更新稿件状态为最终决定状态
                Manuscript manuscript = process.getManuscript();
                manuscript.setStatus(finalDecision);
                manuscriptRepo.save(manuscript);

                // 更新稿件处理流程记录
                process.setComments(comment);
                process.setFinishedAt(LocalDateTime.now());
                return processMapper.entityToVO(manuscriptProcessRepo.save(process), null, null);
            }
            default -> throw new RuntimeException("最终决定只能是接受或拒绝");
        }
    }

    /**
     * 编辑完成初审，编辑做出送审或拒绝的初审决定后调用此方法
     *
     * @param processId       稿件处理流程ID
     * @param initialDecision 初审决定状态
     * @param comment         备注
     * @return 更新后的稿件处理流程记录
     */
    @Transactional
    public ManuscriptProcessVO finishedInitialReview(
            Long processId, Manuscript.ManuscriptStatus initialDecision, String comment) {
        ManuscriptProcess process = manuscriptProcessRepo.getReferenceById(processId);
        if (!process.getStage().equals(ManuscriptProcess.Stage.INITIAL_REVIEW))
            throw new RuntimeException("当前流程阶段不允许做初审");

        switch (initialDecision) {
            case UNDER_REVIEW, REJECTED -> {
                // 更新稿件状态为初审决定状态
                Manuscript manuscript = process.getManuscript();
                manuscript.setStatus(initialDecision);
                manuscriptRepo.save(manuscript);

                // 更新稿件处理流程记录
                process.setComments(comment);
                process.setFinishedAt(LocalDateTime.now());
                return processMapper.entityToVO(manuscriptProcessRepo.save(process), null, null);
            }
            default -> throw new RuntimeException("初审决定只能是送审或拒绝");
        }
    }

    public Review getReviewByProcessId(Long processId) {
        return reviewRepo.findReviewByProcessId(processId);
    }

    /**
     * 获取指定审稿人的所有待处理评审任务
     *
     * @param reviewerId 审稿人ID
     * @return 待处理评审任务列表
     */
    public List<ManuscriptProcessVO> getReviewTasksByReviewerId(Long reviewerId) {
        List<ManuscriptProcess> tasks = manuscriptProcessRepo
                .findManuscriptProcessByProcessedBy_Id(reviewerId);
        return tasks.stream()
                .filter(
                        // 过滤出同行评审阶段且未完成的任务
                        p -> p.getStage().equals(ManuscriptProcess.Stage.PEER_REVIEW)
                                && p.getFinishedAt() == null
                )
                .sorted(
                        // 按处理时间降序排序
                        (p1, p2) -> p2.getProcessedAt().compareTo(p1.getProcessedAt())
                )
                .map(p -> processMapper.entityToVO(p, null, null))
                .toList();
    }

    /**
     * 获取指定编辑的所有待处理编辑评审任务
     *
     * @param editorId 编辑ID
     * @return 待处理编辑评审任务列表
     */
    public List<ManuscriptProcessVO> getEditorReviewTasksByEditorId(Long editorId) {
        List<ManuscriptProcess> tasks = manuscriptProcessRepo
                .findManuscriptProcessByProcessedBy_Id(editorId);
        return tasks.stream().filter(
                        // 过滤出编辑评审阶段且未完成的任务
                        p -> p.getStage().equals(ManuscriptProcess.Stage.EDITORIAL_REVIEW)
                                && p.getFinishedAt() == null
                )
                .sorted(
                        // 按处理时间降序排序
                        (p1, p2) -> p2.getProcessedAt().compareTo(p1.getProcessedAt())
                )
                .map(p -> processMapper.entityToVO(p, null, null))
                .toList();
    }

    /**
     * 获取指定编辑的所有待处理最终决定任务
     *
     * @param editorId 编辑ID
     * @return 待处理最终决定任务列表
     */
    public List<ManuscriptProcessVO> getFinalDecisionTasksByEditorId(Long editorId) {
        List<ManuscriptProcess> tasks = manuscriptProcessRepo
                .findManuscriptProcessByProcessedBy_Id(editorId);
        return tasks.stream().filter(
                        // 过滤出最终决定阶段且未完成的任务
                        p -> p.getStage().equals(ManuscriptProcess.Stage.FINAL_DECISION)
                                && p.getFinishedAt() == null
                )
                .sorted(
                        // 按处理时间降序排序
                        (p1, p2) -> p2.getProcessedAt().compareTo(p1.getProcessedAt())
                )
                .map(p -> processMapper.entityToVO(p, null, reviewRepo))
                .toList();
    }

    /**
     * 获取指定编辑的所有待处理初审任务
     *
     * @param editorId 编辑ID
     * @return 待处理初审任务列表
     */
    public List<ManuscriptProcessVO> getInitialReviewTasksByEditorId(Long editorId) {
        List<ManuscriptProcess> tasks = manuscriptProcessRepo
                .findManuscriptProcessByProcessedBy_Id(editorId);
        return tasks.stream().filter(
                        // 过滤出初审阶段且未完成的任务
                        p -> p.getStage().equals(ManuscriptProcess.Stage.INITIAL_REVIEW)
                                && p.getFinishedAt() == null
                )
                .sorted(
                        // 按处理时间降序排序
                        (p1, p2) -> p2.getProcessedAt().compareTo(p1.getProcessedAt())
                )
                .map(p -> processMapper.entityToVO(p, null, reviewRepo)).toList();
    }


}
