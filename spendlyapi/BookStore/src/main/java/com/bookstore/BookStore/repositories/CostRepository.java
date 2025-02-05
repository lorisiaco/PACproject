package com.bookstore.BookStore.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookstore.BookStore.models.Cost;

@Repository
public interface CostRepository extends JpaRepository<Cost, Integer> {

    List<Cost> findByUserId(Integer userId);

    // Aggiungi questo metodo per trovare i costi di un gruppo
    List<Cost> findByGroupId(Long groupId);
}

