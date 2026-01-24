package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.ManuscriptProcess;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/19 11:25
 * @description 稿件处理流程仓库接口
 */
@Repository
public interface ManuscriptProcessRepo extends JpaRepository<ManuscriptProcess, Long> {

//    @Query("SELECT mp FROM ManuscriptProcess mp WHERE mp.manuscript.id = :manuscriptId ORDER BY mp.processedAt DESC")
    List<ManuscriptProcess> findByManuscriptId(Long manuscriptId);

    List<ManuscriptProcess> findManuscriptProcessByProcessedBy_Id(Long processorId);
}
