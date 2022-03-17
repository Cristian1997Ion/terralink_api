package com.terralink.terralink_api.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import com.terralink.terralink_api.TerralinkApiApplication;
import com.terralink.terralink_api.domain.auth.config.WebSecurityConfig;
import com.terralink.terralink_api.domain.auth.service.JWTService;
import com.terralink.terralink_api.domain.shared.validation.exception.ValidationException;
import com.terralink.terralink_api.domain.user.entity.User;
import com.terralink.terralink_api.domain.user.entity.User_;
import com.terralink.terralink_api.domain.user.repository.UserRepository;
import com.terralink.terralink_api.http.api.ApiResponse;
import com.terralink.terralink_api.http.auth.payload.LoginPayload;
import com.terralink.terralink_api.http.auth.request.LoginRequest;
import com.terralink.terralink_api.http.auth.request.RegisterRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TerralinkApiApplication.class})
@AutoConfigureWebClient
@Import(WebSecurityConfig.class)
public class LoginTests {

    @Autowired
    private WebTestClient webTestClient;

    private static final String LOGIN_URL = "/auth/login";

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void testLoginValidUser() {
        LoginRequest request = new LoginRequest("TestUser", "testPassword1#");

        // mock user in db
        User mockUser = new User(0, request.getUsername(), "", this.bCryptPasswordEncoder.encode(request.getPassword()), true);
        when(
            this.userRepository.findByUsername(eq(request.getUsername()))
        ).thenReturn(Mono.just(mockUser));

        String mockToken = "mockToken";
        when(
            this.jwtService.generateToken(eq(mockUser))
        ).thenReturn(mockToken);

        this.webTestClient
            .post()
            .uri(LOGIN_URL)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody(ApiResponse.class)
            .consumeWith(exchangeResult -> {
               ApiResponse<Map<String, ?>> response = exchangeResult.getResponseBody();
               assert response != null;
               assert response.getSuccess();
               assert response.getPayload().containsKey("username");
               assert response.getPayload().get("username").equals(request.getUsername());
               assert response.getPayload().containsKey("token");
               assert response.getPayload().get("token").equals(mockToken);
            });
    }

    @Test
    void testLoginInvalidPassword() {
        LoginRequest request = new LoginRequest("TestUser", "invalidPassword");

        // mock user in db
        User mockUser = new User(0, request.getUsername(), "", this.bCryptPasswordEncoder.encode("acutalPassword"), true);
        when(
            this.userRepository.findByUsername(eq(request.getUsername()))
        ).thenReturn(Mono.just(mockUser));

        this.webTestClient
        .post()
        .uri(LOGIN_URL)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(ApiResponse.class)
        .consumeWith(exchangeResult -> {
           ApiResponse<?> response = exchangeResult.getResponseBody();
           assert response != null;
           assert ! response.getSuccess();
           assert response.getMessage().equals(ValidationException.DEFAULT_ERROR_MESSAGE);
        });
    }

    @Test
    void testLoginInvalidUsername() {
        LoginRequest request = new LoginRequest("invalidUsername", "password");

        when(
            this.userRepository.findByUsername(eq(request.getUsername()))
        ).thenReturn(Mono.empty());

        this.webTestClient
        .post()
        .uri(LOGIN_URL)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(ApiResponse.class)
        .consumeWith(exchangeResult -> {
           ApiResponse<?> response = exchangeResult.getResponseBody();
           assert response != null;
           assert ! response.getSuccess();
           assert response.getMessage().equals(ValidationException.DEFAULT_ERROR_MESSAGE);
        });
    }
}

  