package com.terralink.terralink_api;

import java.util.Properties;

import javax.persistence.Persistence;

import com.terralink.terralink_api.domain.auth.config.SecurityContextRepository;
import com.terralink.terralink_api.domain.auth.service.AuthService;
import com.terralink.terralink_api.domain.auth.service.JWTService;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
public class TerralinkApiApplication {

    @Autowired
    private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(TerralinkApiApplication.class, args);
	}

    @Bean
    public Mutiny.SessionFactory sessionFactory() {
        
        Properties props = new Properties();
        props.setProperty("javax.persistence.jdbc.url", this.env.getProperty("javax.persistence.jdbc.url"));
        props.setProperty("javax.persistence.jdbc.user", this.env.getProperty("javax.persistence.jdbc.user"));
        props.setProperty("javax.persistence.jdbc.password", this.env.getProperty("javax.persistence.jdbc.password"));

        return Persistence
            .createEntityManagerFactory("terralinkPU", props)
            .unwrap(Mutiny.SessionFactory.class);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public AuthService authService() {
        return new AuthService(new JWTService());
    }

    @Bean 
    public SecurityContextRepository securityContextRepository(AuthService authService) {
        return new SecurityContextRepository(authService);
    }

}
