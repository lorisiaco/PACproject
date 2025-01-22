package com.bmt.spendly.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bmt.spendly.models.Group;


@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {

}
