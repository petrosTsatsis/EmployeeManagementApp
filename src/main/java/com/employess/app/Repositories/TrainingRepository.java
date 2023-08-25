package com.employess.app.Repositories;

import com.employess.app.Entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Integer> {
}
