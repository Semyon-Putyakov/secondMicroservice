package com.example.SecondMicroservice.kafka;

import com.example.SecondMicroservice.dto.PersonDTO;
import com.example.SecondMicroservice.service.PersonService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
        String key = record.key();
        String[] parts = key.split("_");
        String typeOperations = parts[0];
        switch (typeOperations) {
            case "getPersonByUsername":
                personService.getPersonByUsername(record.value().getUsername());
            case "createPerson":
                personService.createPerson(record.value());
        }
    }
}
