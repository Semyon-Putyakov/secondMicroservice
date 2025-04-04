package com.example.SecondMicroservice.service;

import com.example.SecondMicroservice.dto.PersonDTO;
import com.example.SecondMicroservice.kafka.KafkaProducer;
import com.example.SecondMicroservice.model.PersonModel;
import com.example.SecondMicroservice.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private PersonService personService;

    private PersonDTO testPersonDTO;
    private PersonModel testPersonModel;

    @BeforeEach
    void setUp() {
        testPersonDTO = new PersonDTO();
        testPersonDTO.setId(1);
        testPersonDTO.setUsername("testUser");

        testPersonModel = new PersonModel();
        testPersonModel.setId(1);
        testPersonModel.setUsername("testUser");
    }

    @Test
    void getPersonByUsername_WhenPersonExists_ShouldSendToKafka() {
        Mockito.when(personRepository.findByUsername("testUser")).thenReturn(Optional.of(testPersonModel));
        Mockito.when(modelMapper.map(any(PersonModel.class), eq(PersonDTO.class))).thenReturn(testPersonDTO);

        personService.getPersonByUsername("testUser");

        Mockito.verify(kafkaProducer).send(any());
    }

    @Test
    void getPersonById_WhenPersonExists_ShouldSendToKafka() {
        Mockito.when(personRepository.findById(1)).thenReturn(Optional.of(testPersonModel));
        Mockito.when(modelMapper.map(any(PersonModel.class), eq(PersonDTO.class))).thenReturn(testPersonDTO);

        personService.getPersonById(1);

        Mockito.verify(kafkaProducer).send(any());
    }

    @Test
    void createPerson_ShouldSaveToRepository() {
        Mockito.when(modelMapper.map(any(PersonDTO.class), eq(PersonModel.class))).thenReturn(testPersonModel);

        personService.createPerson(testPersonDTO);

        Mockito.verify(personRepository).save(testPersonModel);
    }

    @Test
    void updatePerson_WhenPersonExists_ShouldUpdateAndSave() {
        Mockito.when(personRepository.findById(1)).thenReturn(Optional.of(testPersonModel));

        personService.updatePerson(testPersonDTO);

        Mockito.verify(personRepository).save(testPersonModel);
        Assertions.assertEquals(testPersonDTO.getUsername(), testPersonModel.getUsername());
    }

    @Test
    void deletePerson_WhenPersonExists_ShouldDeleteFromRepository() {
        Mockito.when(personRepository.findById(1)).thenReturn(Optional.of(testPersonModel));

        personService.deletePerson(1);

        Mockito.verify(personRepository).delete(testPersonModel);
    }

    @Test
    void getPersonByUsername_WhenPersonDoesNotExist_ShouldSendNullModel() {
        Mockito.when(personRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        personService.getPersonByUsername("nonexistent");

        Mockito.verify(kafkaProducer).send(any());
    }

    @Test
    void getPersonById_WhenPersonDoesNotExist_ShouldSendNullModel() {
        Mockito.when(personRepository.findById(999)).thenReturn(Optional.empty());
        personService.getPersonById(999);

        Mockito.verify(kafkaProducer).send(any());
    }
} 