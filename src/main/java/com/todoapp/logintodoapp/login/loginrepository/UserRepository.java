package com.todoapp.logintodoapp.login.loginrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.todoapp.logintodoapp.login.loginentity.Users;

public interface UserRepository extends JpaRepository<Users, Long> { 

    @Query("SELECT u FROM Users u WHERE u.email = ?1")
    Users findByEmail(String email);
}
