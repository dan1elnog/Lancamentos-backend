package com.integration.integration.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.integration.integration.enums.StatusEnum;
import com.integration.integration.enums.TypeEnum;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "launch")
@Data
@Builder
public class LaunchModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Max(100)
    @NotNull
    private String description;
    
    @Min(1)
    @Max(12)
    @NotNull
    private Integer month;

    @Min(1990)
    @Max(2022)
    @NotNull
    private Integer year;

    @NotNull
    private BigDecimal value;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeEnum type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserModel user;

    private LocalDateTime registry_date = LocalDateTime.now();

}
