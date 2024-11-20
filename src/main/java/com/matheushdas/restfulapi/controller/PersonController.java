package com.matheushdas.restfulapi.controller;

import com.matheushdas.restfulapi.dto.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.PersonResponse;
import com.matheushdas.restfulapi.dto.UpdatePersonRequest;
import com.matheushdas.restfulapi.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.matheushdas.restfulapi.util.MediaType.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(produces = { JSON, XML, YML })
    public ResponseEntity<List<PersonResponse>> getAllPeople() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping(value = "/{id}", produces = { JSON, XML, YML })
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping(produces = { JSON, XML, YML }, consumes = { JSON, XML, YML })
    public ResponseEntity<PersonResponse> createPerson(@RequestBody CreatePersonRequest person) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(personService.save(person));
    }

    @PutMapping(produces = { JSON, XML, YML }, consumes = { JSON, XML, YML })
    public ResponseEntity<PersonResponse> updatePerson(@RequestBody UpdatePersonRequest person) {
        return ResponseEntity.ok(personService.update(person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
