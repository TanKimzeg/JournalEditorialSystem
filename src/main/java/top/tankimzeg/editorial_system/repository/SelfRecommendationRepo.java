package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.SelfRecommendation;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/19 17:58
 * @description 作者自荐为审稿专家数据访问接口
 */
@Repository
public interface SelfRecommendationRepo extends JpaRepository<SelfRecommendation, Long> {

    List<SelfRecommendation> findSelfRecommendationByApplicantId(Long authorId);

    List<SelfRecommendation> findSelfRecommendationByApproverId(Long editorId);
}
