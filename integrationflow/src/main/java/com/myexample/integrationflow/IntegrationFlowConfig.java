package com.myexample.integrationflow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
public class IntegrationFlowConfig {

    @Bean
    public MessageChannel myInputChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow fileReadingFlow(MessageChannel myInputChannel) {
        return IntegrationFlows.from(Files.inboundAdapter(new File("/tmp/if"))
                                .preventDuplicates(true)
                                .useWatchService(true)
//                                .fileNameGenerator(rename())
                                .autoCreateDirectory(true),
                        e -> e.poller(p -> p.fixedDelay(1000)))
                .transform(String.class, String::toUpperCase)
                .channel((message, timeout) -> {
                    System.out.println(message);
                    return true;
                })
                .get();
    }

}