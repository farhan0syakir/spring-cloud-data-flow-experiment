package com.myexperiment.myfunction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
@SpringBootApplication
public class MyfunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyfunctionApplication.class, args);
    }
    @Bean
    public Function<String, String> toUpperCase() {
        return input -> {
            return input.toUpperCase();
        };
    }
}
