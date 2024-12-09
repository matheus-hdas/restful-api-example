package com.matheushdas.restfulapi.unit.service;

import com.matheushdas.restfulapi.dto.person.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.person.PersonResponse;
import com.matheushdas.restfulapi.dto.person.UpdatePersonRequest;
import com.matheushdas.restfulapi.exception.RequiredObjectIsNullException;
import com.matheushdas.restfulapi.exception.ResourceNotFoundException;
import com.matheushdas.restfulapi.mapper.PersonMapper;
import com.matheushdas.restfulapi.model.Person;
import com.matheushdas.restfulapi.repository.PersonRepository;
import com.matheushdas.restfulapi.service.PersonService;
import com.matheushdas.restfulapi.unit.mock.PersonMockProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    private PersonMockProvider input;

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @BeforeEach
    void setUp() {
        input = new PersonMockProvider();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPersonResponseWithHateoasLink_whenFindById() {
        Person person = input.mockEntity(1);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personMapper.toResponse(person)).thenReturn(input.mockVO(1));

        PersonResponse result = personService.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/person/1>;rel=\"self\"]"));

        assertEquals(person.getId(), result.getKey());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getGender(), result.getGender());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenFindByIdWithNoRecordedId() {
        when(personRepository.findById(0L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            personService.findById(0L);
        });
    }

    @Test
    void shouldCreateEntityAndSendPersonResponseWithHateoasLink_whenSave() {
        CreatePersonRequest request = input.mockCreateRequest(1);
        Person person = input.mockEntity(1);


        when(personMapper.toEntity(request)).thenReturn(person);
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponse(person)).thenReturn(input.mockVO(1));

        PersonResponse result = personService.save(request);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/person/1>;rel=\"self\"]"));

        assertEquals(person.getId(), result.getKey());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getGender(), result.getGender());
    }

    @Test
    void shouldThrowRequiredObjectIsNullException_whenCreateWithNull() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            personService.save(null);
        });
    }

    @Test
    void shouldPatchEntityAndSendPersonResponseWithHateoasLink_whenUpdate() {
        UpdatePersonRequest request = input.mockUpdateRequest(1);
        Person person = input.mockEntity(1);

        when(personRepository.findById(request.id())).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponse(person)).thenReturn(input.mockVO(1));

        PersonResponse result = personService.update(request);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/person/1>;rel=\"self\"]"));

        assertEquals(person.getId(), result.getKey());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getGender(), result.getGender());
    }

    @Test
    @Order(6)
    void shouldThrowRequiredObjectIsNullException_whenUpdateWithNull() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            personService.update(null);
        });
    }
}
