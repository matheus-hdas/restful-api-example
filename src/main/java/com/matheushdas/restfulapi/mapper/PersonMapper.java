package com.matheushdas.restfulapi.mapper;

import com.matheushdas.restfulapi.dto.person.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.person.PersonResponse;
import com.matheushdas.restfulapi.dto.person.UpdatePersonRequest;
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
                data.getGender(),
                data.getEnabled()
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
                data.gender(),
                true
        );
    }

    public Person toEntity(UpdatePersonRequest data) {
        return new Person(
                data.id(),
                data.firstName(),
                data.lastName(),
                data.address(),
                data.gender(),
                true

        );
    }
}
