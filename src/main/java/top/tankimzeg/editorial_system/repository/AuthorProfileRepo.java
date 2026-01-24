package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.AuthorProfile;

/**
 * @author Kim
 * @date 2026/1/19 12:04
 * @description 作者、审稿专家个人资料数据访问接口
 */
@Repository
public interface AuthorProfileRepo extends JpaRepository<AuthorProfile, Long> {


}
