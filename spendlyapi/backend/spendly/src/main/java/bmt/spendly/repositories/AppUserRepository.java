package bmt.spendly.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bmt.spendly.models.AppUser;



@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    /**
     * Cerca un utente per username, ignorando maiuscole e minuscole.
     */
    @Query("SELECT u FROM AppUser u WHERE LOWER(u.username) = LOWER(:username)")
    AppUser findByUsernameIgnoreCase(@Param("username") String username);

	public AppUser findByEmail(String email);
    Optional<AppUser> findByUsername(String username);
}
