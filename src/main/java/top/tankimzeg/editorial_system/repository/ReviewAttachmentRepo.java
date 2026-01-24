package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.ReviewAttachment;

/**
 * @author Kim
 * @date 2026/1/23 12:44
 * @description 评审附件仓库接口
 */
@Repository
public interface ReviewAttachmentRepo extends JpaRepository<ReviewAttachment, String> {
}
