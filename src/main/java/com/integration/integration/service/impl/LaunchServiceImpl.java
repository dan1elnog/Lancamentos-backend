package com.integration.integration.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.integration.integration.enums.TypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integration.integration.enums.StatusEnum;
import com.integration.integration.exceptions.BusinessRuleException;
import com.integration.integration.models.LaunchModel;
import com.integration.integration.repositories.LaunchRepository;
import com.integration.integration.service.interfaces.LaunchService;

@Service
public class LaunchServiceImpl implements LaunchService {

    @Autowired
    private LaunchRepository repository;

    @Override
    @Transactional
    public LaunchModel save(LaunchModel launch) {
        validate(launch);
        launch.setStatus(StatusEnum.PENDENTE);
        return repository.save(launch);
    }

    @Override
    @Transactional
    public LaunchModel update(LaunchModel launch) {
        try {
            repository.findById(launch.getId()).map(entity -> {
                entity.setYear(launch.getYear());
                entity.setDescription(launch.getDescription());
                entity.setMonth(launch.getMonth());
                entity.setValue(launch.getValue());
                entity.setUser(launch.getUser());
                entity.setStatus(launch.getStatus());
                entity.setType(launch.getType());
                entity.setUser(launch.getUser());

                return repository.save(entity);
            }).get();
        }catch (BusinessRuleException e){
            throw new BusinessRuleException("erro");
        }

        return launch;
    }

    @Override
    @Transactional
    public void delete(LaunchModel launch) {
        Objects.requireNonNull(launch.getId());
        repository.delete(launch);
    }

    @Override
    @Transactional(readOnly = true) // transações apenas de consultas
    public List<LaunchModel> search(LaunchModel launchFilter) {
        Example example = Example.of(launchFilter, 
            ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(StringMatcher.CONTAINING)            
            );
        return repository.findAll(example);
    }

    @Override
    public void updateStatus(LaunchModel launch, StatusEnum status) {
        launch.setStatus(status);
        update(launch);
    }

    @Override
    public void validate(LaunchModel launch) {
        if (launch.getDescription() == null || launch.getDescription().trim().equals("")){
            throw new BusinessRuleException("Type a valid description");
        }        

        if (launch.getMonth() == null || launch.getMonth() < 1 || launch.getMonth() > 12 ) {
            throw new BusinessRuleException("Type a valid month");
        }

        if(launch.getYear() == null || launch.getYear().toString().length() != 4 ){
            throw new BusinessRuleException("Type a valid year");
        }

        if (launch.getUser() == null || launch.getUser().getId() == null) {
            throw new BusinessRuleException("User not Valid");
        }
        // Modo para tratar de comparações com valores menores que zero 
        if (launch.getValue() == null || launch.getValue().compareTo(BigDecimal.ZERO) < 1) {
            throw new BusinessRuleException("Value not Valid");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalanceByUser(Long id) {
        BigDecimal revenues = repository.getBalanceByTypeAndUser(id, TypeEnum.RECEITA);
        BigDecimal expenses = repository.getBalanceByTypeAndUser(id, TypeEnum.DESPESA);
        if (expenses == null){
            expenses = BigDecimal.ZERO;
        }
        if (revenues == null){
            revenues = BigDecimal.ZERO;
        }
        return revenues.subtract(expenses);
    }

    public Optional<LaunchModel> findById(long id){
        return repository.findById(id);
    }
}
