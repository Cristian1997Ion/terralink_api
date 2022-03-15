package com.terralink.terralink_api;

import java.util.Properties;

import javax.persistence.Persistence;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

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

}
