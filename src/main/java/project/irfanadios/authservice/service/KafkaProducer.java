package project.irfanadios.authservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import project.irfanadios.authservice.dto.SimpleEmailDto;

@Service
public class KafkaProducer {
    private static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    private ObjectMapper mapper = new ObjectMapper();

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Send uuid for profile-service after creating account.
     * @param uuid
     */
    public void sendMessageStringUUID(String uuid) {
        logger.info("Message send with uuid: {}", uuid);
        Message<String> message = MessageBuilder.withPayload(uuid).setHeader(KafkaHeaders.TOPIC, topicName).build();
        kafkaTemplate.send(message);
    }

    /**
     * Send simple mail. Use EmailDto class to send.
     * Don't insert HTML element here.
     * @param emailDto
     */
    public void sendMessageSimpleMail(SimpleEmailDto emailDto) {
        logger.info("Message send with json: {}", emailDto);
        try {
            String json = mapper.writeValueAsString(emailDto);
            Message<String> message = MessageBuilder.withPayload(json).setHeader(KafkaHeaders.TOPIC, "auth-email-topics").build();
            kafkaTemplate.send(message);
        } catch (JsonProcessingException e) {
            logger.error("JSON Process Error: {}", e.getMessage());
        }
    }
}
