package com.example.SecondMicroservice.repository;

import com.example.SecondMicroservice.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonModel, Integer> {
    Optional<PersonModel> findByUsername(String username);
}
