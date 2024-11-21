package com.matheushdas.restfulapi.mapper;

import com.matheushdas.restfulapi.controller.PersonController;
import com.matheushdas.restfulapi.dto.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.PersonResponse;
import com.matheushdas.restfulapi.dto.UpdatePersonRequest;
import com.matheushdas.restfulapi.model.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonMapper {

    public PersonResponse toResponse(Person data) {

        return new PersonResponse(
                data.getId(),
                data.getFirstName(),
                data.getLastName(),
                data.getAddress(),
                data.getGender()
        );
    }

    public List<PersonResponse> toResponseList(List<Person> data) {
        List<PersonResponse> response = new ArrayList<>();
        for(Person p : data) {
            response.add(this.toResponse(p));
        }

        return response;
    }

    public Person toEntity(CreatePersonRequest data) {
        return new Person(
                data.firstName(),
                data.lastName(),
                data.address(),
                data.gender()
        );
    }

    public Person toEntity(UpdatePersonRequest data) {
        return new Person(
                data.id(),
                data.firstName(),
                data.lastName(),
                data.address(),
                data.gender()
        );
    }
}
