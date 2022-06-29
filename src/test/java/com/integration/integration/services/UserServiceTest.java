package com.integration.integration.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.integration.integration.models.UserModel;
import com.integration.integration.repositories.UserRepository;
import com.integration.integration.service.interfaces.UserService;

@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    UserRepository repository;

    @Autowired
    UserService service;

    @Test 
    public void mustValidateAnUser(){
        String email = "teste11@teste.com";
        String password = "teste3";

        UserModel.builder()
            .name("u2")
            .email(email)
            .password(password)
            .build();
        UserModel result = service.auth(email, password);
        Assertions.assertThat(result).isNotNull();
            
    }

}
