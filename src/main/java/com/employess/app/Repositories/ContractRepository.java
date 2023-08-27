package com.employess.app.Repositories;

import com.employess.app.Entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
}
