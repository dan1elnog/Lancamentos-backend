package com.integration.integration.service.interfaces;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.integration.integration.models.UserModel;

@Service
public interface UserService {
    
    UserModel auth(String email, String password);

    UserModel saveUser(UserModel user);

    void emailValidation(String email);

    Optional<UserModel> findById(Long id);
}
