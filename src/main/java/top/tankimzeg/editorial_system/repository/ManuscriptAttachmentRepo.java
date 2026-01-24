package top.tankimzeg.editorial_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tankimzeg.editorial_system.entity.ManuscriptAttachment;

import java.util.List;

@Repository
public interface ManuscriptAttachmentRepo extends JpaRepository<ManuscriptAttachment, String> {
    List<ManuscriptAttachment> findByManuscriptId(Long manuscriptId);
}


