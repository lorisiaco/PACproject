package spendly.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import spendly.models.UserApp;



public interface UserRepository extends JpaRepository<UserApp,Integer>{
    
    public UserApp findByEmail(String email);
    
}
