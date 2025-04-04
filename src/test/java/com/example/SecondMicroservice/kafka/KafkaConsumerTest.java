package com.example.SecondMicroservice.kafka;

import com.example.SecondMicroservice.dto.PersonDTO;
import com.example.SecondMicroservice.service.PersonService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    private PersonDTO testPersonDTO;

    @BeforeEach
    void setUp() {
        testPersonDTO = new PersonDTO();
        testPersonDTO.setId(1);
        testPersonDTO.setUsername("testUser");
    }

    @Test
    void listen_GetPersonByUsername_ShouldCallService() {
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>(
            "topic_request",
            0,
            0,
            "getPersonByUsername_test",
            testPersonDTO
        );

        kafkaConsumer.listen(record);

        Mockito.verify(personService).getPersonByUsername(testPersonDTO.getUsername());
    }

    @Test
    void listen_GetPersonById_ShouldCallService() {
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>(
            "topic_request",
            0,
            0,
            "getPersonById_1",
            testPersonDTO
        );

        kafkaConsumer.listen(record);

        Mockito.verify(personService).getPersonById(testPersonDTO.getId());
    }

    @Test
    void listen_CreatePerson_ShouldCallService() {
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>(
            "topic_request",
            0,
            0,
            "createPerson",
            testPersonDTO
        );

        kafkaConsumer.listen(record);

        Mockito.verify(personService).createPerson(testPersonDTO);
    }

    @Test
    void listen_UpdatePerson_ShouldCallService() {
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>(
            "topic_request",
            0,
            0,
            "updatePerson",
            testPersonDTO
        );

        kafkaConsumer.listen(record);

        Mockito.verify(personService).updatePerson(testPersonDTO);
    }

    @Test
    void listen_DeletePerson_ShouldCallService() {
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>(
            "topic_request",
            0,
            0,
            "deletePerson",
            testPersonDTO
        );

        kafkaConsumer.listen(record);

        Mockito.verify(personService).deletePerson(testPersonDTO.getId());
    }

    @Test
    void listen_WithNullKey_ShouldNotCallService() {
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>(
            "topic_request",
            0,
            0,
            null,
            testPersonDTO
        );

        kafkaConsumer.listen(record);

        Mockito.verifyNoInteractions(personService);
    }

    @Test
    void listen_WithEmptyKey_ShouldNotCallService() {
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>(
            "topic_request",
            0,
            0,
            "",
            testPersonDTO
        );

        kafkaConsumer.listen(record);

        Mockito.verifyNoInteractions(personService);
    }

    @Test
    void listen_WithInvalidOperation_ShouldNotCallService() {
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>(
            "topic_request",
            0,
            0,
            "invalidOperation",
            testPersonDTO
        );

        kafkaConsumer.listen(record);

        Mockito.verifyNoInteractions(personService);
    }
} 