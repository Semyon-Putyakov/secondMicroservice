package com.example.SecondMicroservice.kafka;

import com.example.SecondMicroservice.dto.PersonDTO;
import com.example.SecondMicroservice.service.PersonService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final PersonService personService;

    @Autowired
    public KafkaConsumer(PersonService personService) {
        this.personService = personService;
    }

    @KafkaListener(topics = "topic_request", groupId = "request")
    public void listen(ConsumerRecord<String, PersonDTO> record) {

        System.out.println(record.value().toString());
        System.out.println(record.key());
        String key = record.key();
        if (key == null) {
            return;
        }
        String[] parts = key.split("_");
        if (parts.length == 0) {
            return;
        }

        String typeOperations = parts[0];

        switch (typeOperations) {
            case "getPersonByUsername":
                System.out.println("kafkaConsumer getPersonByUsername");
                personService.getPersonByUsername(record.value().getUsername());
                break;
            case "createPerson":
                System.out.println("kafkaConsumer createPerson");
                personService.createPerson(record.value());
                break;
        }
    }
}
