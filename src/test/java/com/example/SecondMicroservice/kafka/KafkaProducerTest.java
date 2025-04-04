package com.example.SecondMicroservice.kafka;

import com.example.SecondMicroservice.dto.PersonDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, PersonDTO> kafkaTemplate;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    private PersonDTO testPersonDTO;
    private ProducerRecord<String, PersonDTO> testRecord;

    @BeforeEach
    void setUp() {
        testPersonDTO = new PersonDTO();
        testPersonDTO.setId(1);
        testPersonDTO.setUsername("testUser");

        testRecord = new ProducerRecord<>(
            "topic_response",
            "test_key",
            testPersonDTO
        );
    }

    @Test
    void send_ShouldCallKafkaTemplateSend() {
        kafkaProducer.send(testRecord);

        Mockito.verify(kafkaTemplate, times(1)).send(
            testRecord.topic(),
            testRecord.key(),
            testRecord.value()
        );
    }

    @Test
    void send_WithNullValue_ShouldCallKafkaTemplateSend() {
        ProducerRecord<String, PersonDTO> recordWithNullValue = new ProducerRecord<>(
            "topic_response",
            "test_key",
            null
        );

        kafkaProducer.send(recordWithNullValue);

        Mockito.verify(kafkaTemplate, times(1)).send(
            recordWithNullValue.topic(),
            recordWithNullValue.key(),
            recordWithNullValue.value()
        );
    }

    @Test
    void send_WithEmptyTopic_ShouldCallKafkaTemplateSend() {
        ProducerRecord<String, PersonDTO> recordWithEmptyTopic = new ProducerRecord<>(
            "",
            "test_key",
            testPersonDTO
        );

        kafkaProducer.send(recordWithEmptyTopic);

        Mockito.verify(kafkaTemplate, times(1)).send(
            recordWithEmptyTopic.topic(),
            recordWithEmptyTopic.key(),
            recordWithEmptyTopic.value()
        );
    }

    @Test
    void send_WithNullKey_ShouldCallKafkaTemplateSend() {
        ProducerRecord<String, PersonDTO> recordWithNullKey = new ProducerRecord<>(
            "topic_response",
            null,
            testPersonDTO
        );

        kafkaProducer.send(recordWithNullKey);

        Mockito.verify(kafkaTemplate, times(1)).send(
            recordWithNullKey.topic(),
            recordWithNullKey.key(),
            recordWithNullKey.value()
        );
    }
} 