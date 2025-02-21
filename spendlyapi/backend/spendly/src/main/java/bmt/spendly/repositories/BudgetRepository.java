package bmt.spendly.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bmt.spendly.models.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // Trovare un Budget via userId
    Budget findByUserId(int userId);

    // Se preferisci trovarlo via username, potresti usare una query custom:
    // @Query("SELECT b FROM Budget b WHERE LOWER(b.user.username) = LOWER(:username)")
    // Budget findByUsername(@Param("username") String username);
    
}
