package com.matheushdas.restfulapi.unit.mock;

import com.matheushdas.restfulapi.dto.person.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.person.PersonResponse;
import com.matheushdas.restfulapi.dto.person.UpdatePersonRequest;
import com.matheushdas.restfulapi.model.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonMockProvider {
    public Person mockEntity() {
        return mockEntity(0);
    }

    public PersonResponse mockVO() {
        return mockVO(0);
    }

    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonResponse> mockVOList() {
        List<PersonResponse> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }

    public Person mockEntity(Integer number) {
        return new Person(
                number.longValue(),
                "First Name Test" + number,
                "Last Name Test" + number,
                "Address Test" + number,
                ((number % 2)==0) ? "Male" : "Female"
        );
    }

    public CreatePersonRequest mockCreateRequest(Integer number) {
        return new CreatePersonRequest(
                "First Name Test" + number,
                "Last Name Test" + number,
                "Address Test" + number,
                ((number % 2)==0) ? "Male" : "Female"
        );
    }

    public UpdatePersonRequest mockUpdateRequest(Integer number) {
        return new UpdatePersonRequest(
                number.longValue(),
                "First Name Test" + number,
                "Last Name Test" + number,
                "Address Test" + number,
                ((number % 2)==0) ? "Male" : "Female"
        );
    }

    public PersonResponse mockVO(Integer number) {
        return new PersonResponse(
                number.longValue(),
                "First Name Test" + number,
                "Last Name Test" + number,
                "Address Test" + number,
                ((number % 2)==0) ? "Male" : "Female"
        );
    }
}
