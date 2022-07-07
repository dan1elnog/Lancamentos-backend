package com.integration.integration.service.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.integration.integration.enums.StatusEnum;
import com.integration.integration.enums.TypeEnum;
import com.integration.integration.models.LaunchModel;
import com.sun.jdi.connect.LaunchingConnector;
import org.springframework.web.bind.annotation.RequestParam;

public interface LaunchService {
    
    LaunchModel save(LaunchModel launch);

    LaunchModel update(LaunchModel launch);
    
    void delete(LaunchModel launchModel);
    
    List<LaunchModel> search(LaunchModel launchFilter);
    
    void  updateStatus(LaunchModel launch, StatusEnum status);

    void  validate(LaunchModel launch);
    Optional<LaunchModel> getById(Long id);
    BigDecimal getBalanceByUser(Long id);

    List<LaunchModel> filter(
            @RequestParam(value = "keyword", required = false) Integer keyword);


}
