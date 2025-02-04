package com.bookstore.BookStore.repositories;

import com.bookstore.BookStore.models.AppUser;
import com.bookstore.BookStore.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Trova le spese in base all'utente che le ha create
    List<Expense> findByCreatedBy(AppUser createdBy);
}
