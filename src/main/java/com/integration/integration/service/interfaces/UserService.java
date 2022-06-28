package com.integration.integration.service.interfaces;

import org.springframework.stereotype.Service;

import com.integration.integration.models.UserModel;

@Service
public interface UserService {
    
    UserModel auth(String email, String password);

    UserModel saveUser(UserModel user);

    void emailValidation(String email);
}
