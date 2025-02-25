package bmt.spendly.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bmt.spendly.models.AppUser;
import bmt.spendly.models.Savings;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {
    List<Savings> findByUser(AppUser user);
}

