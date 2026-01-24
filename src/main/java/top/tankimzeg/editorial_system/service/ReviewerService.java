package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tankimzeg.editorial_system.entity.AuthorProfile;
import top.tankimzeg.editorial_system.repository.ReviewerProfileRepo;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/19 22:05
 * @description 管理审稿专家服务类
 */
@Service
public class ReviewerService {

    @Autowired
    private ReviewerProfileRepo reviewerProfileRepo;

    /**
     * 为编辑呈现所有审稿专家资料，后续可以添加筛选功能
     *
     * @return 审稿专家资料列表
     */
    public List<AuthorProfile> getAllReviewers() {
        return reviewerProfileRepo.findAll();
    }
}
