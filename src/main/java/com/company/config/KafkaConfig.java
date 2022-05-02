package com.company.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.company.enums.TopicEnums.LIBRARY_EVENTS;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic libraryEvent() {
        return TopicBuilder.name(LIBRARY_EVENTS.getInfo())
                .partitions(3)
                .replicas(3)
                .build();
    }
}
