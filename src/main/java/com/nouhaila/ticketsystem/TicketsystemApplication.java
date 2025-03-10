package com.nouhaila.ticketsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



@SpringBootApplication
@EntityScan(basePackages = "com.nouhaila.ticketsystem.model")
@ComponentScan(basePackages = "com.nouhaila.ticketsystem")
@EnableJpaRepositories(basePackages = "com.nouhaila.ticketsystem.repository")
public class TicketsystemApplication {

    public static void main(String[] args) {
       var context = SpringApplication.run(TicketsystemApplication.class, args);    
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}

