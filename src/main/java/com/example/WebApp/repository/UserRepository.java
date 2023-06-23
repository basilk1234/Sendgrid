package com.example.WebApp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.WebApp.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("update User u set u.email = ?1, u.password = ?2 where u.id = ?3")
    int updateEmailAndPasswordById(String email, String password, Long id);

    Optional<User> findByEmailAndPassword(String email, String password);

    @Override
    void deleteById(Long aLong);
}
