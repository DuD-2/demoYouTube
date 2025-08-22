package com.rangers.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rangers.demo.dto.JwtAuthenticationDto;
import com.rangers.demo.dto.RefreshTokenDto;
import com.rangers.demo.dto.UserCredentialsDto;
import com.rangers.demo.security.jwt.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void signInTest() throws Exception {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setEmail("test@gmail.com");
        userCredentialsDto.setPassword("12345");

        String userJson = objectMapper.writeValueAsString(userCredentialsDto);

        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto jwtAuthenticationDto = objectMapper.readValue(tokenJson, JwtAuthenticationDto.class);

        Assertions.assertEquals(userCredentialsDto.getEmail(), jwtService.getEmailFromToken(jwtAuthenticationDto.getToken()));
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void signInNegativeTest() throws Exception {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setEmail("test@gmail.com");
        userCredentialsDto.setPassword("125555");

        String userJson = objectMapper.writeValueAsString(userCredentialsDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void refresh() throws Exception {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setEmail("test@gmail.com");
        userCredentialsDto.setPassword("12345");

        String userJson = objectMapper.writeValueAsString(userCredentialsDto);

        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();

        refreshTokenDto.setRefreshToken(objectMapper.readValue(tokenJson, JwtAuthenticationDto.class).getRefreshToken());

        String refreshTokenJson = objectMapper.writeValueAsString(refreshTokenDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshTokenJson))
                .andExpect(status().isOk());
    }
}