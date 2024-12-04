package com.matheushdas.restfulapi.controller;

import com.matheushdas.restfulapi.dto.person.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.person.PersonResponse;
import com.matheushdas.restfulapi.dto.person.UpdatePersonRequest;
import com.matheushdas.restfulapi.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.matheushdas.restfulapi.util.MediaType.*;

@RestController
@RequestMapping("/api/person")
@Tag(name = "People", description = "Endpoints to managing people")
public class PersonController {
    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(produces = { JSON, XML, YML })
    @Operation(
            summary = "Finds All People",
            description = "Return all person list",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = { @Content(
                                    mediaType = JSON,
                                    array = @ArraySchema(schema = @Schema(
                                            implementation = PersonResponse.class)
                                    )
                            ) }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<List<PersonResponse>> getAllPeople() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping(value = "/{id}", produces = { JSON, XML, YML })
    @Operation(
            summary = "Find Person By Id",
            description = "Search on DB a person with provided id",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = { @Content(
                                    schema = @Schema(implementation = PersonResponse.class)
                            ) }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping(produces = { JSON, XML, YML }, consumes = { JSON, XML, YML })
    @Operation(
            summary = "Create a new Person",
            description = "Save a person on DB",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = { @Content(
                                    schema = @Schema(implementation = PersonResponse.class)
                            ) }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PersonResponse> createPerson(@RequestBody @Valid CreatePersonRequest person) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(personService.save(person));
    }

    @PutMapping(produces = { JSON, XML, YML }, consumes = { JSON, XML, YML })
    @Operation(
            summary = "Updates a person",
            description = "Patch any person field",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = { @Content(
                                    schema = @Schema(implementation = PersonResponse.class)
                            ) }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PersonResponse> updatePerson(@RequestBody @Valid UpdatePersonRequest person) {
        return ResponseEntity.ok(personService.update(person));
    }

    @PatchMapping(value = "/{id}", produces = { JSON, XML, YML })
    @Operation(
            summary = "Disable a person",
            description = "Patch any person field",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = { @Content(
                                    schema = @Schema(implementation = PersonResponse.class)
                            ) }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PersonResponse> disablePerson(@PathVariable Long id) {
        return ResponseEntity.ok(personService.disablePerson(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletes a person",
            description = "Remove a person by provided id",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "No Content",
                            responseCode = "204"
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
