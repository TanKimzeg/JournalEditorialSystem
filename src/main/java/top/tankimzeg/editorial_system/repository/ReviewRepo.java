package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.Review;

/**
 * @author Kim
 * @date 2026/1/19 11:48
 * @description 审稿意见数据访问接口
 */
@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    Review findReviewByProcessId(Long processId);

}
