package com.myexample.s3tokafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class S3ToKafkaApplication implements ApplicationRunner {
    @Autowired
    private KafkaProducerService producerService;

    public static void main(String[] args) {
        SpringApplication.run(S3ToKafkaApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        producerService.sendMessage("this is testing message");
    }
}