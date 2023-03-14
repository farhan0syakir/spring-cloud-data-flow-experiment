package com.myexample.mydemosource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@SpringBootApplication
public class MyDemoSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyDemoSourceApplication.class, args);
    }

    @Bean
    public Supplier<String> graalSupplier() {
        final String[] splitWoodchuck = "How much wood could a woodchuck chuck if a woodchuck could chuck wood?"
                .split(" ");
        final AtomicInteger wordsIndex = new AtomicInteger(0);
        return () -> {
            int wordIndex = wordsIndex.getAndAccumulate(splitWoodchuck.length,
                    (curIndex, numWords) -> curIndex < numWords - 1 ? curIndex + 1 : 0);
            return splitWoodchuck[wordIndex];
        };
    }

}