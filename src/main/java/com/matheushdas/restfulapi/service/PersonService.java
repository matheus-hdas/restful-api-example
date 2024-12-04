package com.matheushdas.restfulapi.service;

import com.matheushdas.restfulapi.controller.PersonController;
import com.matheushdas.restfulapi.dto.person.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.person.PersonResponse;
import com.matheushdas.restfulapi.exception.RequiredObjectIsNullException;
import com.matheushdas.restfulapi.exception.ResourceNotFoundException;
import com.matheushdas.restfulapi.mapper.PersonMapper;
import com.matheushdas.restfulapi.dto.person.UpdatePersonRequest;
import com.matheushdas.restfulapi.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {
    private PersonRepository personRepository;
    private PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public List<PersonResponse> findAll() {
        return personMapper
                .toResponseList(personRepository.findAll())
                .stream()
                .map(p -> p.add(
                        linkTo(
                                methodOn(PersonController.class)
                                        .getPersonById(p.getKey()))
                                .withSelfRel()))
                .toList();
    }

    public PersonResponse findById(Long id) {
        PersonResponse result =
                personMapper.toResponse(personRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")));
        result.add(
                linkTo(
                        methodOn(PersonController.class)
                                .getPersonById(result.getKey()))
                        .withSelfRel());
        return result;
    }

    public PersonResponse save(CreatePersonRequest person) {
        if(person == null) throw new RequiredObjectIsNullException("Person cannot be null!");
        PersonResponse result = personMapper.toResponse(
                personRepository.save(personMapper.toEntity(person))
        );
        result.add(
                linkTo(
                        methodOn(PersonController.class)
                                .getPersonById(result.getKey()))
                        .withSelfRel());
        return result;
    }

    public PersonResponse update(UpdatePersonRequest person) {
        if (person == null) throw new RequiredObjectIsNullException("Person cannot be null!");

        PersonResponse result = personMapper.toResponse(
                personRepository.findById(person.id())
                        .map(toUpdate -> {
                            toUpdate.setFirstName(person.firstName());
                            toUpdate.setLastName(person.lastName());
                            toUpdate.setAddress(person.address());
                            toUpdate.setGender(person.gender());
                            return personRepository.save(toUpdate);
                        })
                        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"))
        );

        result.add(
                linkTo(
                        methodOn(PersonController.class)
                                .getPersonById(result.getKey()))
                        .withSelfRel()
        );
        return result;
    }


    @Transactional
    public PersonResponse disablePerson(Long id) {
        personRepository.disablePerson(id);
        PersonResponse result =
                personMapper.toResponse(personRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!")));
        result.add(
                linkTo(
                        methodOn(PersonController.class)
                                .getPersonById(result.getKey()))
                        .withSelfRel()
        );
        return result;
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}
