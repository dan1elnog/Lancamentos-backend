package com.integration.integration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integration.integration.models.LaunchModel;

public interface LaunchRepository extends JpaRepository<LaunchModel, Long> {
    
}
