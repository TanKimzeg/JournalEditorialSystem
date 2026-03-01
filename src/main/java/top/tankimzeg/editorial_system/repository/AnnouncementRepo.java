package top.tankimzeg.editorial_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import top.tankimzeg.editorial_system.entity.Announcement;

public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {

    Page<Announcement> findByStatus(Announcement.Status status, Pageable pageable);
}

