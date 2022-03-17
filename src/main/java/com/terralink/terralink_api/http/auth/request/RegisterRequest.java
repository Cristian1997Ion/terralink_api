package com.terralink.terralink_api.http.auth.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.terralink.terralink_api.domain.shared.validation.constraint.Unique;
import com.terralink.terralink_api.domain.user.entity.User_;
import com.terralink.terralink_api.domain.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterRequest {
    @NotNull
    @Size(min = 4, max = 9)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "must be alphanumerical")
    @Pattern(regexp = "^[a-zA-Z].*$", message = "must start with a letter")
    @Unique(entityField = User_.USERNAME, entityRepository = UserRepository.class)
    private String username;

    @NotNull
    @Email
    @Unique(entityField = User_.EMAIL, entityRepository = UserRepository.class)
    private String email;

    @NotNull
    @Size(min = 6, max = 20)
    @Pattern(regexp = ".*[a-z].*$", message = "must contain at least one lowercase letter")
    @Pattern(regexp = ".*[A-Z].*$", message = "must contain at least one uppercase letter")
    @Pattern(regexp = ".*[0-9].*$", message = "must contain at least one digit")
    private String password;
}
