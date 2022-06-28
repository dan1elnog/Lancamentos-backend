package com.integration.integration.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integration.integration.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    
    public Boolean existsByEmail(String email);

    public Optional<UserModel> findByEmail(String email);
}
