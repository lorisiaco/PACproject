package bmt.spendly.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bmt.spendly.models.Alert;
import bmt.spendly.models.Group;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByGroup(Group group);
}
