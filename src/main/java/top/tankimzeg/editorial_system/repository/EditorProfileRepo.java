package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.EditorProfile;

/**
 * @author Kim
 * @date 2026/1/19 17:51
 * @description 编辑个人资料数据访问接口
 */
@Repository
public interface EditorProfileRepo extends JpaRepository<EditorProfile, Long> {

}
