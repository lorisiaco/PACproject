package com.bookstore.BookStore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.BookStore.models.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {

}