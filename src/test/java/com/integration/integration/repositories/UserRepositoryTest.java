package com.integration.integration.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.integration.integration.models.UserModel;
import com.integration.integration.service.interfaces.UserService;

@SpringBootTest
public class UserRepositoryTest {
    
    @Autowired
    UserRepository repository;

    @Autowired
    UserService service;

    @Test
    public void checkIfExistsEmail(){
    //Cenário
    UserModel.builder()
    .name("daniel")
    .email("teste@email.com")
    .password("1234")
    .build();
    //Ação / Execução
        boolean result = repository.existsByEmail("daniel.00msn@gmail.com");
    //Verificação
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void checkIfNotExistsEmail(){
        boolean result = repository.existsByEmail("teste@email.com");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void persistUserOnDatabase(){
        UserModel user = UserModel.builder()
            .name("daniel2")
            .email("teste2@email.com")
            .password("1234")
            .build();
        UserModel userSaved = repository.save(user);
        Assertions.assertThat(userSaved.getId()).isNotNull();
    }

    @Test
    public void mustFindAnUserByEmail(){
        UserModel user = UserModel.builder()
            .name("ola")
            .email("ola@gmail.com")
            .password("213522")
            .build();
            Optional<UserModel> searcher = repository.findByEmail(user.getEmail());
            Assertions.assertThat(searcher).isNotNull();
    }

    @Test
    public void saveUser(){
        UserModel user = UserModel.builder()
            .name("ola")
            .email("ola@gmail.com")
            .password("213522")
            .build();
        
        user.getEmail();
        UserModel u = service.saveUser(user);
        Assertions.assertThat(u).isEqualTo(true);        
    }
}
