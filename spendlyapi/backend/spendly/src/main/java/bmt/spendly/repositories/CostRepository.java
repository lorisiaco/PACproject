package bmt.spendly.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bmt.spendly.models.Cost;

@Repository
public interface CostRepository extends JpaRepository<Cost, Long> { // MODIFICATO da Integer a Long

    List<Cost> findByUserId(Integer userId); // MODIFICATO da Integer a Long

    List<Cost> findByGroupId(Long groupId); // MODIFICATO da Integer a Long
}

