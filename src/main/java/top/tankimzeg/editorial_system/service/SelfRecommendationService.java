package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tankimzeg.editorial_system.dto.response.SelfRecommendationVO;
import top.tankimzeg.editorial_system.entity.SelfRecommendation;
import top.tankimzeg.editorial_system.entity.User;
import top.tankimzeg.editorial_system.mapper.SelfRecommendationMapper;
import top.tankimzeg.editorial_system.repository.SelfRecommendationRepo;
import top.tankimzeg.editorial_system.repository.UserRepo;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/19 17:54
 * @description 作者自荐为审稿专家服务类
 */
@Service
public class SelfRecommendationService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EditorService editorService;

    @Autowired
    private SelfRecommendationRepo selfRecommendationRepo;

    private static final SelfRecommendationMapper selfReccomendationMapper = SelfRecommendationMapper.INSTANCE;
    private static final SelfRecommendationMapper selfRecommendationMapper = SelfRecommendationMapper.INSTANCE;

    /**
     * 作者提交自荐申请
     *
     * @param author      作者信息
     * @param applyReason 自荐理由
     * @return 保存的自荐申请记录
     */
    @Transactional
    public SelfRecommendationVO apply(User author, String applyReason) {
        SelfRecommendation selfRecommendation = new SelfRecommendation();
        selfRecommendation.setApplicant(author);
        User editor = editorService.assignToSelfRecommendation(author.getId());
        selfRecommendation.setApprover(editor);
        selfRecommendation.setStatus(SelfRecommendation.Status.PENDING);
        selfRecommendation.setApplyReason(applyReason);
        return selfRecommendationMapper.entityToVO(
                selfRecommendationRepo.save(selfRecommendation)
        );
    }

    /**
     * 审批自荐申请
     *
     * @param recommendationId 自荐申请ID
     * @param approved         审批结果
     * @param comment          审批意见
     * @return 更新后的自荐申请记录
     */
    @Transactional
    public SelfRecommendationVO approve(
            Long recommendationId, boolean approved, String comment) {
        SelfRecommendation selfRecommendation = selfRecommendationRepo.findById(recommendationId)
                .orElseThrow(() -> new RuntimeException("自荐申请不存在"));
        selfRecommendation.setStatus(approved ? SelfRecommendation.Status.ACCEPTED : SelfRecommendation.Status.REJECTED);
        selfRecommendation.setComments(comment);
        if (approved) {
            User upgradeAuthor = userRepo.findById(selfRecommendation.getApplicant().getId()).orElseThrow();
            upgradeAuthor.setRole(User.Role.REVIEWER);
            userRepo.save(upgradeAuthor);
        }
        SelfRecommendation savedEntity = selfRecommendationRepo.save(selfRecommendation);
        return selfRecommendationMapper.entityToVO(savedEntity);
    }

    /**
     * 获取指定编辑的待处理自荐申请列表
     *
     * @param editorId 编辑ID
     * @return 待处理自荐申请列表
     */
    public List<SelfRecommendationVO> getPendingTasks(Long editorId) {
        return selfRecommendationRepo.findSelfRecommendationByApproverId(editorId)
                .stream().filter(
                        rec -> rec.getStatus()
                                .equals(SelfRecommendation.Status.PENDING)
                ).sorted(
                        // 按申请时间降序排序
                        (r1, r2) -> r2.getRequestedAt().compareTo(r1.getRequestedAt())
                ).map(selfRecommendationMapper::entityToVO)
                .toList();
    }

    /**
     * 获取指定作者的自荐申请记录列表
     *
     * @param authorId 作者ID
     * @return 自荐申请记录列表
     */
    public List<SelfRecommendationVO> getAuthorApplications(Long authorId) {
        return selfRecommendationRepo.findSelfRecommendationByApplicantId(authorId)
                .stream().sorted(
                        // 按申请时间降序排序
                        (r1, r2) -> r2.getRequestedAt().compareTo(r1.getRequestedAt())
                ).map(selfRecommendationMapper::entityToVO)
                .toList();
    }

}
