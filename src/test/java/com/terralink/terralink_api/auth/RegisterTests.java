package com.terralink.terralink_api.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.terralink.terralink_api.TerralinkApiApplication;
import com.terralink.terralink_api.domain.auth.config.WebSecurityConfig;
import com.terralink.terralink_api.domain.shared.validation.exception.ValidationException;
import com.terralink.terralink_api.domain.user.entity.User;
import com.terralink.terralink_api.domain.user.entity.User_;
import com.terralink.terralink_api.domain.user.repository.UserRepository;
import com.terralink.terralink_api.http.api.ApiResponse;
import com.terralink.terralink_api.http.auth.request.RegisterRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TerralinkApiApplication.class})
@AutoConfigureWebClient
@Import(WebSecurityConfig.class)
public class RegisterTests {

    @Autowired
    private WebTestClient webTestClient;

    private static final String REGISTER_URL = "/auth/register";

    @MockBean
    private UserRepository userRepository;

    @Test
    void testRegisteringValidUser() {
        RegisterRequest request = new RegisterRequest("TestUser", "TestEmail@emailtest.com", "testPassword1#");

        // mock uniqiue fields
        when(
            this.userRepository.existsWithAttribute(anyString(), anyString())
        ).thenReturn(Mono.just(false));

        when(
            this.userRepository.save(any())
        ).thenReturn(Mono.just(new User()));

        this.webTestClient
            .post()
            .uri(REGISTER_URL)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ApiResponse.class)
            .consumeWith(exchangeResult -> {
               ApiResponse<?> response = exchangeResult.getResponseBody();
               assert response != null;
               assert response.getSuccess();
            });
    }

    @Test
    void testRegisteringEmailNotUnique() {
        RegisterRequest request = new RegisterRequest("TestUser", "TestEmail@emailtest.com", "testPassword1#");

        when(
            this.userRepository.existsWithAttribute(eq(User_.USERNAME), anyString())
        ).thenReturn(Mono.just(false));

        when(
            this.userRepository.existsWithAttribute(eq(User_.EMAIL), anyString())
        ).thenReturn(Mono.just(true));


        this.webTestClient
        .post()
        .uri(REGISTER_URL)
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
    void testRegisteringUsernameNotUnique() {
        RegisterRequest request = new RegisterRequest("TestUser", "TestEmail@emailtest.com", "testPassword1#");

        when(
            this.userRepository.existsWithAttribute(eq(User_.EMAIL), anyString())
        ).thenReturn(Mono.just(false));

        when(
            this.userRepository.existsWithAttribute(eq(User_.USERNAME), anyString())
        ).thenReturn(Mono.just(true));


        this.webTestClient
        .post()
        .uri(REGISTER_URL)
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
    void testRegisteringInvalidUsername() {
        RegisterRequest request = new RegisterRequest("5555___$$$", "TestEmail@emailtest.com", "testPassword1#");

        // mock uniqiue fields
        when(
            this.userRepository.existsWithAttribute(anyString(), anyString())
        ).thenReturn(Mono.just(false));

        this.webTestClient
            .post()
            .uri(REGISTER_URL)
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
    void testRegisteringInvalidEmail() {
        RegisterRequest request = new RegisterRequest("TestUser", "TestEmom", "testPassword1#");

        // mock uniqiue fields
        when(
            this.userRepository.existsWithAttribute(anyString(), anyString())
        ).thenReturn(Mono.just(false));

        this.webTestClient
            .post()
            .uri(REGISTER_URL)
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
    void testRegisteringInvalidPassword() {
        RegisterRequest request = new RegisterRequest("TestUser", "TestEmail@emailtest.com", "");

        // mock uniqiue fields
        when(
            this.userRepository.existsWithAttribute(anyString(), anyString())
        ).thenReturn(Mono.just(false));

        this.webTestClient
            .post()
            .uri(REGISTER_URL)
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
