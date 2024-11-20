package com.matheushdas.restfulapi.service;

import com.matheushdas.restfulapi.dto.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.PersonResponse;
import com.matheushdas.restfulapi.mapper.PersonMapper;
import com.matheushdas.restfulapi.dto.UpdatePersonRequest;
import com.matheushdas.restfulapi.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public List<PersonResponse> findAll() {
        return personMapper.toResponseList(personRepository.findAll());
    }

    public PersonResponse findById(Long id) {
        return personMapper.toResponse(personRepository.findById(id).orElseThrow());
    }

    public PersonResponse save(CreatePersonRequest person) {
        return personMapper.toResponse(
                personRepository.save(
                        personMapper.toEntity(person)
                )
        );
    }

    public PersonResponse update(UpdatePersonRequest person) {
        if(personRepository.existsById(person.id())) {
            return personMapper.toResponse(personRepository.save(personMapper.toEntity(person)));
        }
        throw new RuntimeException();
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}
