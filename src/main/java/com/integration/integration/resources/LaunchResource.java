package com.integration.integration.resources;


import javax.transaction.Transactional;

import com.integration.integration.dto.ChangeStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integration.integration.dto.LaunchDto;
import com.integration.integration.enums.StatusEnum;
import com.integration.integration.enums.TypeEnum;
import com.integration.integration.exceptions.BusinessRuleException;
import com.integration.integration.models.LaunchModel;
import com.integration.integration.models.UserModel;
import com.integration.integration.service.impl.LaunchServiceImpl;
import com.integration.integration.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/api/launch")
@RequiredArgsConstructor
public class LaunchResource {

    private final LaunchServiceImpl service;
    private final UserServiceImpl userService;

    //Método para fazer uma consulta
    @GetMapping
    public ResponseEntity<?> search(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam("user") Long id){

        LaunchModel filterLaunch = new LaunchModel();
        filterLaunch.setDescription(description);
        filterLaunch.setMonth(month);
        filterLaunch.setYear(year);


        Optional<UserModel> user =  userService.findById(id);
        if (!user.isPresent()){
            return ResponseEntity.badRequest().body("User Not Found");
        }else{
            filterLaunch.setUser(user.get());
        }
        List<LaunchModel> launchs = service.search(filterLaunch);
        return ResponseEntity.ok(filterLaunch);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> save(@RequestBody LaunchDto dto){
        try {
            LaunchModel newLaunch =  conversor(dto);
            newLaunch = service.save(newLaunch);
            return new ResponseEntity<>(newLaunch, HttpStatus.CREATED);
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(path = "/{id}/change-status")
    public ResponseEntity<?> changeStatus(@PathVariable Long id ,@RequestBody ChangeStatusDto dto){
        return service.findById(id).map(entity -> {
            StatusEnum status = StatusEnum.valueOf(dto.getStatus());
            if (status == null){
                return  ResponseEntity.badRequest().body("The status can't be a null value");
            }
            try {
                entity.setStatus(status);
                service.update(entity);
                return ResponseEntity.ok(entity);
            }catch (BusinessRuleException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() ->
                new  ResponseEntity<>("Launch not Found in Database", HttpStatus.BAD_REQUEST));
    }

    @PutMapping(path="/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody LaunchDto dto) {
        return  service.findById(id).map(entity -> {
            try {
                LaunchModel launch = conversor(dto);
                launch.setId(id);
                service.update(launch);
                return new ResponseEntity<>(launch, HttpStatus.ACCEPTED);
            }catch (BusinessRuleException e){
                return  ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() ->
           new  ResponseEntity<>("Launch not Found in Database", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id){
        return service.findById(id).map(entity -> {
            try {
                service.delete(entity);
                return new ResponseEntity<>( HttpStatus.NO_CONTENT);
            }catch (BusinessRuleException e){
                return  ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () ->
            new ResponseEntity<>("Request Not Answered", HttpStatus.BAD_REQUEST) );
    }

    public LaunchModel conversor(LaunchDto dto){
        LaunchModel newLaunch = new LaunchModel();
        newLaunch.setId(dto.getId());
        newLaunch.setDescription(dto.getDescription());
        newLaunch.setMonth(dto.getMonth());
        newLaunch.setYear(dto.getYear());
        newLaunch.setValue(dto.getValue());
        if (dto.getStatus() != null){
            //Maneira de converter String para Enum
            newLaunch.setStatus(StatusEnum.valueOf(dto.getStatus()));
        }
        if (dto.getType() != null) {
            newLaunch.setType(TypeEnum.valueOf(dto.getType()));
        }

        //Forma de settar um usuário pelo Id
        UserModel user = 
        userService.findById(dto.getUser())
            .orElseThrow(() -> new BusinessRuleException("Invalid User"));
        newLaunch.setUser(user);

        return newLaunch;
    }

}
