package com.myexample.s3tokafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {
    @Value("${application.configs.topic.name}")
    private String TOPIC_NAME;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        log.info("send");
        kafkaTemplate.send(TOPIC_NAME, message.substring(0, 4), message);
    }
}
