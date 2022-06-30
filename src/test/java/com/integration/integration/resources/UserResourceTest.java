package com.integration.integration.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integration.integration.dto.UserDto;
import com.integration.integration.models.UserModel;
import com.integration.integration.service.interfaces.LaunchService;
import com.integration.integration.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@SpringBootTest
@WebMvcTest(controllers = UserResource.class)
@AutoConfigureMockMvc
public class UserResourceTest {

    static final String API = "api/user";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;
    @MockBean
    UserService service;

    @MockBean
    LaunchService launchService;

    @Test
    public void mustAutentifyAUser() throws Exception {
        String email = "teste@email.com.br";
        String password = "teste44";
        UserDto dto = UserDto.builder().email(email).password(password).build();
        UserModel user = UserModel.builder()
                .name("test")
                .password(password)
                .id(22L)
                .registry_date(LocalDateTime.now())
                .email(email)
                .build();
        Mockito.when(service.auth(email, password)).thenReturn(user);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post(API.concat("/auth"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("password").value(user.getPassword()));
    }

}

