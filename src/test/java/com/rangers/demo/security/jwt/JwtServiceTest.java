package com.rangers.demo.security.jwt;

import com.rangers.demo.dto.JwtAuthenticationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final String EMAIL = "test123@gmail.com";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService.setJwtSecret("8074658237c236e39e96e909ac1abb25a3e1773b100096ad6877c439cd452c17");
    }


    @Test
    void generateAuthTokenTest() {
        JwtAuthenticationDto jwtAuthenticationDto = jwtService.generateAuthToken(EMAIL);
        assertEquals(EMAIL, jwtService.getEmailFromToken(jwtAuthenticationDto.getToken()));
        assertTrue(jwtService.validateJwtToken(jwtAuthenticationDto.getToken()));
    }

    @Test
    void refreshBaseTokenTest() throws InterruptedException {
        JwtAuthenticationDto jwtAuthenticationDto = jwtService.generateAuthToken(EMAIL);

        Thread.sleep(1000);

        JwtAuthenticationDto jwtAuthenticationDtoNew = jwtService
                .refreshBaseToken(EMAIL, jwtAuthenticationDto.getRefreshToken());

        assertEquals(jwtAuthenticationDto.getRefreshToken(),jwtAuthenticationDtoNew.getRefreshToken());

        assertNotEquals(jwtAuthenticationDtoNew.getToken(),jwtAuthenticationDto.getToken());
    }

    @Test
    void getEmailFromTokenTest() {
        JwtAuthenticationDto jwtAuthenticationDto = jwtService.generateAuthToken(EMAIL);
        assertEquals(EMAIL, jwtService.getEmailFromToken(jwtAuthenticationDto.getToken()));
    }

    @Test
    void validateJwtTokenTest() {
        JwtAuthenticationDto jwtAuthenticationDto = jwtService.generateAuthToken(EMAIL);
        System.out.println(jwtAuthenticationDto.getToken());
        assertTrue(jwtService.validateJwtToken(jwtAuthenticationDto.getToken()));
    }
    @Test
    void validateJwtTokenNegativeTest() {
        assertFalse(jwtService.validateJwtToken("eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0MTIzQGdtYWlsLmNvbSIsImV4cCI6MTcyO" +
                "TE4ODczNH0.EOfU8t6MJ0ueowWOMdTV2Z4GHILbmn7qWOb5GVpuezYasWfc-Nut2WKiAS37UdWd"));
    }
}