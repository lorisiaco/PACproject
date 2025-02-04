package com.bookstore.BookStore.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.BookStore.models.AppUser;
import com.bookstore.BookStore.models.Cost;

@Repository
public interface CostRepository extends JpaRepository<Cost, Long> {
    List<Cost> findByUser(AppUser user);
}