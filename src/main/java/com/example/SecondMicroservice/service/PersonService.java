package com.example.SecondMicroservice.service;

import com.example.SecondMicroservice.dto.PersonDTO;
import com.example.SecondMicroservice.kafka.KafkaProducer;
import com.example.SecondMicroservice.model.PersonModel;
import com.example.SecondMicroservice.repository.PersonRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final KafkaProducer kafkaProducer;
    private PersonDTO personDTO;
    private PersonModel NullModel = new PersonModel(0, null, null);

    @Autowired
    public PersonService(PersonRepository personRepository, ModelMapper modelMapper, KafkaProducer kafkaProducer) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.kafkaProducer = kafkaProducer;

    }

    @Transactional(readOnly = true)
    public void getPersonByUsername(String username) {
        PersonModel personModel = personRepository.findByUsername(username).orElse(null);
        PersonDTO personDTO = mapper(personModel);
        String key = "getPersonByUsername_" + personDTO.getUsername();
        ProducerRecord<String, PersonDTO> record = new ProducerRecord<>("topic_response", key, personDTO);
        kafkaProducer.send(record);
    }

    @Transactional(readOnly = true)
    public void getPersonById(int id) {
        PersonModel personModel = personRepository.findById(id).orElse(null);
        PersonDTO personDTO = mapper(personModel);
        String key = "getPersonById_" + id;
        ProducerRecord<String, PersonDTO> record = new ProducerRecord<>("topic_response", key, personDTO);
        kafkaProducer.send(record);
    }


    public void createPerson(PersonDTO personDTO) {
        PersonModel personModel = modelMapper.map(personDTO, PersonModel.class);
        personRepository.save(personModel);
    }

    public void updatePerson(PersonDTO personDTO) {
        PersonModel personModel = personRepository.findById(personDTO.getId()).orElse(null);
        personModel.setUsername(personDTO.getUsername());
        personRepository.save(personModel);

    }

    public void deletePerson(int id) {
        PersonModel personModel = personRepository.findById(id).orElse(null);
        personRepository.delete(personModel);
    }

    private PersonDTO mapper(PersonModel personModel) {
        if (personModel != null)
            personDTO = modelMapper.map(personModel, PersonDTO.class);
        else
            personDTO = modelMapper.map(NullModel, PersonDTO.class);
        return personDTO;
    }
}
