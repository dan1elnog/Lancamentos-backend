package com.integration.integration.services;

import com.integration.integration.enums.StatusEnum;
import com.integration.integration.exceptions.BusinessRuleException;
import com.integration.integration.models.LaunchModel;
import com.integration.integration.models.UserModel;
import com.integration.integration.repositories.LaunchRepository;
import static com.integration.integration.repositories.LaunchRepositoryTest.*;
import com.integration.integration.service.impl.LaunchServiceImpl;
import org.apache.catalina.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class LaunchServiceTest {

    @SpyBean
    public LaunchServiceImpl service;

    @MockBean
    LaunchRepository repository;

    @Test
    public void mustSaveALaunch(){
        LaunchModel launch = returnALaunch(); // Possível utilizar por ser um método público e estático
        Mockito.doNothing().when(service).validate(launch);
        LaunchModel launchSaved = returnALaunch();
        launchSaved.setId(1L);
        launchSaved.setStatus(StatusEnum.PENDENTE);
        Mockito.when(repository.save(launch)).thenReturn(launchSaved);

        service.save(launch);

        Assertions.assertThat(launch.getId()).isEqualTo(launchSaved.getId());
        Assertions.assertThat(launch.getStatus()).isEqualTo(StatusEnum.PENDENTE);
    }
    @Test
    public void mustNotSaveALaunchWhenThereIsAValidationError(){
        LaunchModel launchToSave = returnALaunch();
        Mockito.doThrow(BusinessRuleException.class).when(service).validate(launchToSave);

        Assertions.catchThrowableOfType(() -> service.save(launchToSave), BusinessRuleException.class );

        Mockito.verify(repository, Mockito.never()).save(launchToSave);
    }

    @Test
    public void mustUpdateALaunch(){
        LaunchModel launchSaved = returnALaunch();
        launchSaved.setId(1L);
        launchSaved.setStatus(StatusEnum.PENDENTE);
        Mockito.doNothing().when(service).validate(launchSaved);

        Mockito.when(repository.save(launchSaved)).thenReturn(launchSaved);

        service.update(launchSaved);

        Mockito.verify(repository, Mockito.times(1)).save(launchSaved);
    }

    @Test
    public void mustDeleteALaunch(){
        LaunchModel launchToSave = returnALaunch();
        launchToSave.setId(1L);

        service.delete(launchToSave);

        Mockito.verify(repository).delete(launchToSave);
    }

    @Test
    public void mustNotDeleteALaunch(){
        LaunchModel launchToSave = returnALaunch();
        launchToSave.setId(null);

        Assertions.catchThrowableOfType(() -> service.delete(launchToSave), NullPointerException.class);

        Mockito.verify(repository, Mockito.never()).delete(launchToSave);
    }

    @Test
    public void mustFilterLaunchs(){
        LaunchModel launch =  returnALaunch();

        List<LaunchModel> list = Arrays.asList(launch);
        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(list);

       List<LaunchModel> result =  service.search(launch);

       Assertions.assertThat(result).isNotEmpty().hasSize(1).contains(launch);
    }

    @Test
    public void mustUpdateAStatus(){
        LaunchModel launch = returnALaunch();
        StatusEnum launchStatus = StatusEnum.CANCELADO;

        Mockito.doReturn(launch).when(service).update(launch);
        service.updateStatus(launch, launchStatus);

        Assertions.assertThat(launch.getStatus()).isEqualTo(launchStatus);
        Mockito.verify(service).update(launch);
    }

    @Test
    public void mustFindAlaunchById(){
        LaunchModel launch = returnALaunch();
        long id =  1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(launch));

        Optional<LaunchModel>  result = service.findById(id);

        Assertions.assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void validateTestErrors(){

        LaunchModel launch = LaunchModel.builder()
                .user(null)
                .status(null)
                .value(null)
                .description(null)
                .year(null)
                .month(null)
                .type(null)
                .build();
        Throwable error =  Assertions.catchThrowable(() -> service.validate(launch) );
        Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("Type a valid description");

        launch.setDescription("salary");
        error =  Assertions.catchThrowable(() -> service.validate(launch) );
        Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("Type a valid month");

        launch.setMonth(2);
        error =  Assertions.catchThrowable(() -> service.validate(launch) );
        Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("Type a valid year");

        launch.setYear(2014);
        error =  Assertions.catchThrowable(() -> service.validate(launch) );
        Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("User not Valid");

        launch.setUser(new UserModel());
        launch.getUser().setId(33L);
        error =  Assertions.catchThrowable(() -> service.validate(launch) );
        Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("Value not Valid");
    }
}






