package com.company.producer;

import com.company.model.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.company.enums.TopicEnums.LIBRARY_EVENTS;

@Component
@Slf4j
@RequiredArgsConstructor
public class LibraryEventProducer {

    private final KafkaTemplate<Long, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendLibraryEvent(LibraryEvent libraryEvent) {
        try {
            ProducerRecord<Long, String> producerRecord = prepareProducerRecord(libraryEvent);
            kafkaTemplate.send(producerRecord).addCallback(result -> log.info("Event Successfully sent to Kafka topic {}, partition {}", Objects.requireNonNull(result).getRecordMetadata().topic(), result.getRecordMetadata().partition()), ex -> log.error("Failed to send event to Kafka", ex));
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException Sending the Message and the exception is {}", e.getMessage());
        }
    }

    //Asynchronous
    public void sendLibraryEventAsync(LibraryEvent libraryEvent) {
        try {
            kafkaTemplate.sendDefault(libraryEvent.getId(), objectMapper.writeValueAsString(libraryEvent)).addCallback(result -> log.info("Event Successfully sent to Kafka topic {}, partition {}", Objects.requireNonNull(result).getRecordMetadata().topic(), result.getRecordMetadata().partition()), ex -> log.error("Failed to send event to Kafka", ex));
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException Sending the Message and the exception is {}", e.getMessage());
        }
    }

    //Synchronous behaviour
    public SendResult<Long, String> sendLibraryEventSync(LibraryEvent libraryEvent) {
        SendResult<Long, String> sync = null;
        try {
            sync = kafkaTemplate.sendDefault(libraryEvent.getId(), objectMapper.writeValueAsString(libraryEvent)).get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException | JsonProcessingException e) {
            log.error("InterruptedException/ExecutionException Sending the Message and the exception is {}", e.getMessage());
        } catch (Exception e) {
            log.error("Exception Sending the Message and the exception is {}", e.getMessage());
        }
        return sync;
    }


    //prepare ProducerRecord
    private ProducerRecord<Long, String> prepareProducerRecord(LibraryEvent libraryEvent) throws JsonProcessingException {
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<Long, String>(LIBRARY_EVENTS.getInfo(), null, libraryEvent.getId(), objectMapper.writeValueAsString(libraryEvent), recordHeaders);
    }
}
