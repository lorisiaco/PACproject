package com.bookstore.BookStore.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.BookStore.models.AppUser;
import com.bookstore.BookStore.models.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {

        List<Group> findByMembriContains(AppUser user); // Cerca tutti i gruppi in cui l'utente è membro
}