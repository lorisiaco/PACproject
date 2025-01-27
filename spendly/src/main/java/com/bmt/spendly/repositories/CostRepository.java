package com.bmt.spendly.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bmt.spendly.models.Cost;;

@Repository
public interface CostRepository extends JpaRepository<Cost,Long> {
    
}
