package com.employess.app.Repositories;

import com.employess.app.Entities.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenceRepository extends JpaRepository<Absence, Integer> {
}