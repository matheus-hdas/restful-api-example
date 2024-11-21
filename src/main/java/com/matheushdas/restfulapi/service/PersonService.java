package com.matheushdas.restfulapi.service;

import com.matheushdas.restfulapi.controller.PersonController;
import com.matheushdas.restfulapi.dto.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.PersonResponse;
import com.matheushdas.restfulapi.mapper.PersonMapper;
import com.matheushdas.restfulapi.dto.UpdatePersonRequest;
import com.matheushdas.restfulapi.repository.PersonRepository;
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
                personMapper.toResponse(personRepository.findById(id).orElseThrow());
        result.add(
                linkTo(
                        methodOn(PersonController.class)
                                .getPersonById(result.getKey()))
                        .withSelfRel());
        return result;
    }

    public PersonResponse save(CreatePersonRequest person) {
        PersonResponse result = personMapper.toResponse(
                personRepository.save(
                        personMapper.toEntity(person)
                )
        );
        result.add(
                linkTo(
                        methodOn(PersonController.class)
                                .getPersonById(result.getKey()))
                        .withSelfRel());
        return result;
    }

    public PersonResponse update(UpdatePersonRequest person) {
        if(personRepository.existsById(person.id())) {
            PersonResponse result =
                    personMapper.toResponse(
                            personRepository.save(personMapper.toEntity(person)));
            result.add(
                    linkTo(
                            methodOn(PersonController.class)
                                    .getPersonById(result.getKey()))
                            .withSelfRel());
            return result;
        }
        throw new RuntimeException();
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}
