package com.example.SecondMicroservice.service;

import com.example.SecondMicroservice.dto.PersonDTO;
import com.example.SecondMicroservice.kafka.KafkaProducer;
import com.example.SecondMicroservice.model.PersonModel;
import com.example.SecondMicroservice.repository.PersonRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PersonService personService;

    @Captor
    private ArgumentCaptor<ProducerRecord<String, PersonDTO>> producerRecordCaptor;

    @Captor
    private ArgumentCaptor<PersonModel> personModelCaptor;

    @Test
    void testGetPersonByUsername_existingUser() {
        PersonModel personModel = new PersonModel(1, "john", "pass");
        PersonDTO personDTO = new PersonDTO("john", "pass", 1);
        Mockito.when(personRepository.findByUsername("john")).thenReturn(Optional.of(personModel));
        Mockito.when(modelMapper.map(eq(personModel), eq(PersonDTO.class))).thenReturn(personDTO);

        personService.getPersonByUsername("john");

        Mockito.verify(kafkaProducer, times(1)).send(producerRecordCaptor.capture());

        ProducerRecord<String, PersonDTO> record = producerRecordCaptor.getValue();
        Assertions.assertEquals("topic_response", record.topic());
        Assertions.assertEquals("getPersonByUsername_john", record.key());
        Assertions.assertEquals("john", record.value().getUsername());
    }

    @Test
    void testGetPersonByUsername_notExistingUser() {
        PersonDTO nullDTO = new PersonDTO(null, null, 0);

        Mockito.when(personRepository.findByUsername("john")).thenReturn(Optional.empty());
        Mockito.when(modelMapper.map(any(PersonModel.class), eq(PersonDTO.class)))
                .thenReturn(nullDTO);

        personService.getPersonByUsername("john");

        Mockito.verify(kafkaProducer, times(1)).send(producerRecordCaptor.capture());

        ProducerRecord<String, PersonDTO> record = producerRecordCaptor.getValue();
        Assertions.assertEquals("topic_response", record.topic());
        Assertions.assertEquals("getPersonByUsername_null", record.key());
        Assertions.assertNull(record.value().getUsername());
    }

    @Test
    void testGetPersonById_existingId() {
        PersonModel personModel = new PersonModel(1, "john", "pass");
        PersonDTO personDTO = new PersonDTO("john", "pass", 1);
        Mockito.when(personRepository.findById(1)).thenReturn(Optional.of(personModel));
        Mockito.when(modelMapper.map(eq(personModel), eq(PersonDTO.class))).thenReturn(personDTO);

        personService.getPersonById(1);

        Mockito.verify(kafkaProducer, times(1)).send(producerRecordCaptor.capture());

        ProducerRecord<String, PersonDTO> record = producerRecordCaptor.getValue();
        Assertions.assertEquals("getPersonById_1", record.key());
        Assertions.assertEquals(1, record.value().getId());
    }

    @Test
    void testCreatePerson() {
        PersonDTO dto = new PersonDTO("john", "pass", 1);
        PersonModel model = new PersonModel(1, "john", "pass");
        Mockito.when(modelMapper.map(dto, PersonModel.class)).thenReturn(model);

        personService.createPerson(dto);

        Mockito.verify(personRepository, times(1)).save(personModelCaptor.capture());

        PersonModel saved = personModelCaptor.getValue();
        Assertions.assertEquals("john", saved.getUsername());
        Assertions.assertEquals("pass", saved.getPassword());
    }

    @Test
    void testUpdatePerson_existingId() {
        PersonModel personModel = new PersonModel(1, "oldUsername", "pass");
        Mockito.when(personRepository.findById(1)).thenReturn(Optional.of(personModel));

        PersonDTO dto = new PersonDTO("newUsername", "pass", 1);
        personService.updatePerson(dto);

        Mockito.verify(personRepository, times(1)).save(personModelCaptor.capture());

        PersonModel updated = personModelCaptor.getValue();
        Assertions.assertEquals("newUsername", updated.getUsername());
    }

    @Test
    void testDeletePerson_existingId() {
        PersonModel personModel = new PersonModel(1, "john", "pass");
        Mockito.when(personRepository.findById(1)).thenReturn(Optional.of(personModel));

        personService.deletePerson(1);

        Mockito.verify(personRepository, times(1)).delete(personModelCaptor.capture());

        PersonModel deleted = personModelCaptor.getValue();
        Assertions.assertEquals(1, deleted.getId());
    }

}
