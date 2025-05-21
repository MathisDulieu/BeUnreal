package com.supinfo.beunreal_message_service.service;

import com.supinfo.beunreal_message_service.model.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    public void sendPrivateMessage(KafkaMessage kafkaMessage) {

    }

    public void sendGroupMessage(KafkaMessage kafkaMessage) {

    }

}
