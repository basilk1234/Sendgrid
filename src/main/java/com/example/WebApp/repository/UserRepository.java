package com.example.WebApp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.WebApp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
