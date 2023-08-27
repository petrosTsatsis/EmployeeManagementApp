package com.employess.app.Repositories;

import com.employess.app.Entities.Employee;
import com.employess.app.Entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

}
