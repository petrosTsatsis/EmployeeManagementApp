package com.employess.app.Repositories;

import com.employess.app.Entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
}
