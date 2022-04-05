package com.terralink.terralink_api;

import java.util.Properties;

import javax.persistence.Persistence;

import com.terralink.terralink_api.domain.auth.config.SecurityContextRepository;
import com.terralink.terralink_api.domain.auth.service.AuthService;
import com.terralink.terralink_api.domain.auth.service.JWTService;
import com.terralink.terralink_api.domain.shared.gateway.nyt.NYTGateway;
import com.terralink.terralink_api.domain.user.service.UserService;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider.Proxy;

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
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public AuthService authService(JWTService jwtService, UserService userService) {
        return new AuthService(jwtService, userService);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public SecurityContextRepository securityContextRepository(AuthService authService) {
        return new SecurityContextRepository(authService);
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        if (this.env.getProperty("webclient.proxy", "0").equals("1")) {
            HttpClient httpClient = HttpClient.create()
            .proxy(proxy -> 
                proxy
                    .type(Proxy.HTTP)
                    .host(this.env.getProperty("webclient.proxy.host"))
                    .port(Integer.valueOf(this.env.getProperty("webclient.proxy.port")))
            );
        

        return builder
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
        }

        return builder.build();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public NYTGateway nytGateway(WebClient webClient) {
        return new NYTGateway(
            webClient,
            this.env.getProperty("nyt.api.url"),
            this.env.getProperty("nyt.api.secret")
        );
    }

}
