package com.employess.app.Repositories;

import com.employess.app.Entities.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
}
