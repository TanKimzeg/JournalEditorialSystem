package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.Manuscript;

import java.util.List;

/**
 * @author Kim
 * @date 2026/1/19 11:13
 * @description 稿件数据访问接口
 */
@Repository
public interface ManuscriptRepo extends JpaRepository<Manuscript, Long> {

    @Query("SELECT m FROM Manuscript m WHERE m.author.id = :authorId")
    List<Manuscript> findByAuthorId(Long authorId);

}
