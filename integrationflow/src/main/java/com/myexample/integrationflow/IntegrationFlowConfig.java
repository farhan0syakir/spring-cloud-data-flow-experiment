package com.myexample.integrationflow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.MessageChannel;

import javax.jms.ConnectionFactory;

@Configuration
public class IntegrationFlowConfig {

    @Bean
    public MessageChannel myInputChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow jmsInbound(ConnectionFactory connectionFactory) {
        return IntegrationFlows.from(
                        Jms.inboundAdapter(connectionFactory)
                                .destination("inQueue"),
                        e -> e.poller(poller -> poller.fixedRate(1000)))
                .handle(m -> System.out.println(m.getPayload()))
                .get();
    }
}