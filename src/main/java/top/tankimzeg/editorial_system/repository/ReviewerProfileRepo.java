package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.AuthorProfile;

/**
 * @author Kim
 * @date 2026/1/19 22:06
 * @description 评审员档案仓库接口
 */
@Repository
public interface ReviewerProfileRepo extends JpaRepository<AuthorProfile, Long> {

}
