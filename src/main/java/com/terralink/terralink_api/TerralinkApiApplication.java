package com.terralink.terralink_api;

import java.io.InputStream;
import java.util.Properties;

import javax.persistence.Persistence;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class TerralinkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TerralinkApiApplication.class, args);
	}

    @Bean
    public Mutiny.SessionFactory sessionFactory() {
        Properties props = new Properties();
        try (InputStream stream = new ClassPathResource("env.properties").getInputStream()) {
            props.load(stream);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return Persistence
            .createEntityManagerFactory("terralinkPU", props)
            .unwrap(Mutiny.SessionFactory.class);
    }

}
