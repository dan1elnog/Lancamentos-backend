package com.integration.integration.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {
    
    private String email;
    private String name;
    private String password;
}
