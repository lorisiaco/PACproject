package com.bmt.spendly.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmt.spendly.models.AppUser;



public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	public AppUser findByEmail(String email);
}
