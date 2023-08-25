package com.employess.app.Repositories;

import com.employess.app.Entities.Employee;
import com.employess.app.Entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);
}
