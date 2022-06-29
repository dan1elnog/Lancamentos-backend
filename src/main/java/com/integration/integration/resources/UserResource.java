package com.integration.integration.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integration.integration.dto.UserDto;
import com.integration.integration.exceptions.BusinessRuleException;
import com.integration.integration.exceptions.ErrorAuthentication;
import com.integration.integration.models.UserModel;
import com.integration.integration.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/user")
public class UserResource {
    
    @Autowired
    private UserServiceImpl service;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserDto user){
        UserModel newUser = UserModel.builder()
            .email(user.getEmail())
            .name(user.getName())
            .password(user.getPassword())
            .build();
        try {
            UserModel userSaved = service.saveUser(newUser);
            return new ResponseEntity<>(userSaved, HttpStatus.CREATED); // Lógica para implementação de mensagem de erro e de sucesso 
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "/auth")
    public ResponseEntity<?> auth(@RequestBody UserDto dto){
        try {
            UserModel user = service.auth(dto.getEmail(), dto.getPassword());
            return ResponseEntity.ok(user);
        } catch (ErrorAuthentication e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
