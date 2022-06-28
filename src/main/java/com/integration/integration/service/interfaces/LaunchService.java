package com.integration.integration.service.interfaces;

import java.util.List;

import com.integration.integration.enums.StatusEnum;
import com.integration.integration.models.LaunchModel;

public interface LaunchService {
    
    LaunchModel save(LaunchModel launch);

    LaunchModel update(LaunchModel launch);
    
    void delete(LaunchModel launchModel);
    
    List<LaunchModel> search(LaunchModel launchFilter);
    
    void  updateStatus(LaunchModel launch, StatusEnum status);

    void  validate(LaunchModel launch);
}
