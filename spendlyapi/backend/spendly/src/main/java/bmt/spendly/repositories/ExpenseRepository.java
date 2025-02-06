package bmt.spendly.repositories;

import bmt.spendly.models.AppUser;
import bmt.spendly.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Trova le spese in base all'utente che le ha create
    List<Expense> findByCreatedBy(AppUser createdBy);
}
