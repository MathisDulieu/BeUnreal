package com.supinfo.beunreal_media_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supinfo.beunreal_media_service.model.KafkaMessage;
import com.supinfo.beunreal_media_service.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {

    @Autowired
    private ObjectMapper objectMapper;

    private final MediaService messageService;

    @KafkaListener(topics = "media-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeAuthenticationEvents(
            @Payload String messageJson,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {

        try {
            log.info("JSON message received from media-service topic [key: {}, partition: {}, offset: {}]", key, partition, offset);

            KafkaMessage kafkaMessage = objectMapper.readValue(messageJson, KafkaMessage.class);

            handleOperation(key, kafkaMessage);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            acknowledgment.acknowledge();
        }
    }

    private void handleOperation(String operationKey, KafkaMessage kafkaMessage) {
        log.info("Processing operation: {}", operationKey);

        switch (operationKey) {
            case "shareMediaToGroup":
                messageService.shareMediaToGroup(kafkaMessage);
                break;
            case "shareMediaToUser":
                messageService.shareMediaToUser(kafkaMessage);
                break;
            case "postMedia":
                messageService.postMedia(kafkaMessage);
                break;
            default:
                log.warn("Unknown operation: {}", operationKey);
        }
    }
}