package com.integration.integration.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private String email;
    private String name;
    private String password;
}
