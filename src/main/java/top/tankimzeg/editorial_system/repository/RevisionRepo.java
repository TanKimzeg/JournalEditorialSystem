package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.Revision;

/**
 * @author Kim
 * @date 2026/1/19 16:59
 * @description 修订仓库接口
 */
@Repository
public interface RevisionRepo extends JpaRepository<Revision, Long> {

    Revision findRevisionByProcessId(Long processId);
}
