package com.integration.integration.repositories;

import com.integration.integration.enums.StatusEnum;
import com.integration.integration.enums.TypeEnum;
import com.integration.integration.models.LaunchModel;
import com.integration.integration.models.UserModel;
import com.integration.integration.service.impl.LaunchServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class LaunchRepositoryTest {

    @Autowired
    LaunchRepository repository;

    @Autowired
    LaunchServiceImpl service;

    public static LaunchModel returnALaunch(){
        return LaunchModel.builder()
                .id(1L)
                .description("teste")
                .month(2)
                .year(2002)
                .value(BigDecimal.valueOf(10))
                .type(TypeEnum.DESPESA)
                .status(StatusEnum.PENDENTE)
                .registry_date(LocalDateTime.now())
                .build();
    }


    @Test
    public void mustSaveALaunch(){
        LaunchModel launch = returnALaunch();
        launch = repository.save(launch);

        Assertions.assertThat(launch.getId()).isNotNull();
    }

    @Test
    public void deleteALaunch(){
        LaunchModel launch =  returnALaunch();
        repository.delete(launch);
        Assertions.assertThat(launch.getId()).isNull();
    }

    @Test
    public void mustUpdateALaunch(){
        LaunchModel launch = returnALaunch();
        launch.setMonth(5);
        launch.setValue(BigDecimal.valueOf(999));
        launch.setDescription("test2");
        repository.save(launch);

        Assertions.assertThat(launch.getMonth()).isEqualTo(5);
    }

    @Test
    public void mustFindALaunchById(){
        LaunchModel launch = returnALaunch();
        Optional<LaunchModel> launchId = repository.findById(launch.getId());
        Assertions.assertThat(launchId.isPresent()).isTrue();
    }


}
