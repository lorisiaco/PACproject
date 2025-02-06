package bmt.spendly.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bmt.spendly.models.AppUser;
import bmt.spendly.models.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> { // MODIFICATO da Integer a Long
    List<Group> findByMembriContains(AppUser user); // Cerca tutti i gruppi in cui l'utente è membro
}