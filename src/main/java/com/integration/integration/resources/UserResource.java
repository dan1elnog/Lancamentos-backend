package com.integration.integration.resources;

import com.integration.integration.service.interfaces.LaunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integration.integration.dto.UserDto;
import com.integration.integration.exceptions.BusinessRuleException;
import com.integration.integration.exceptions.ErrorAuthentication;
import com.integration.integration.models.UserModel;
import com.integration.integration.service.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserResource {
    
    @Autowired
    private UserServiceImpl service;

    @Autowired
    private LaunchService launchService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserDto user){
        UserModel newUser = UserModel.builder()
            .email(user.getEmail())
            .name(user.getName())
            .password(user.getPassword())
            .registry_date(LocalDateTime.now())
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

    @GetMapping("/{id}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long id){
        Optional<UserModel> user =  service.getById(id);
        if (!user.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BigDecimal balance =  launchService.getBalanceByUser(id);
        return  ResponseEntity.ok(balance);
    }
}
