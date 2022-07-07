package com.integration.integration.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integration.integration.exceptions.BusinessRuleException;
import com.integration.integration.exceptions.ErrorAuthentication;
import com.integration.integration.models.UserModel;
import com.integration.integration.repositories.UserRepository;
import com.integration.integration.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    @Override
    public UserModel auth(String email, String password) {
        Optional<UserModel> userEmail = repository.findByEmail(email);
        
        if (!userEmail.isPresent()) {
            throw new ErrorAuthentication("Email unknown");
        }

        if (!userEmail.get().getPassword().equals(password)) {
            throw new ErrorAuthentication("Invalid password");
        }
        return userEmail.get();
    }

    @Override
    @Transactional
    public UserModel saveUser(UserModel user) {
        emailValidation(user.getEmail());
        return repository.save(user);
    }

    @Override
    public void emailValidation(String email) {
        boolean exists = repository.existsByEmail(email);
        if (exists) {
            throw new BusinessRuleException("There is already an registered email like this");
        }
    }
    
    public Optional<UserModel> getById(Long id){
        return repository.findById(id);
    }
}
