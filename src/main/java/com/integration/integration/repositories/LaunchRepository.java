package com.integration.integration.repositories;

import com.integration.integration.enums.TypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import com.integration.integration.models.LaunchModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LaunchRepository extends JpaRepository<LaunchModel, Long> {

    @Query(
            value = " SELECT SUM(l.value) FROM LaunchModel l" +
                    " JOIN l.user u WHERE u.id = :idUser AND l.type = :type GROUP BY u "
    )
    BigDecimal getBalanceByTypeAndUser(@Param("idUser") Long idUser, @Param("type") TypeEnum type);
    // Método de Query personalizada onde é usado o JPQL como linguagem de consulta e ":value" como uma variável
    // de busca, sendo a mesma recuperada atavés do "@Param("value")"
}
