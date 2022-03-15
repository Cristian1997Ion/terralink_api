package com.terralink.terralink_api.domain.user.service;

import com.terralink.terralink_api.domain.user.entity.User;
import com.terralink.terralink_api.domain.user.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public Mono<User> findUserByCredentials(String username, String password) {
        return this
            .userRepository
            .findByUsername(username)
            .filter(user -> this.passwordEncoder.matches(password, user.getPassword()));
    }

    public Mono<User> createUser(
        String username,
        String email,
        String password,
        Boolean enabled
    ) {
        return this.userRepository.save(new User(
            null, 
            username,
            email,
            this.passwordEncoder.encode(password),
            enabled
        ));
    }

}