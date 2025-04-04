package com.example.SecondMicroservice.kafka;

import com.example.SecondMicroservice.dto.PersonDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, PersonDTO> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, PersonDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(ProducerRecord<String, PersonDTO> record) {
        kafkaTemplate.send(record.topic(), record.key(), record.value());
    }
}
