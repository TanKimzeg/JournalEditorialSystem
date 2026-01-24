package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.RevisionAttachment;

/**
 * @author Kim
 * @date 2026/1/23 12:34
 * @description 修订附件仓库接口
 */
@Repository
public interface RevisionAttachmentRepo extends JpaRepository<RevisionAttachment, String> {

}
