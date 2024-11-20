package com.matheushdas.restfulapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@JsonPropertyOrder({"id", "first_name", "last_name", "address", "gender", "_links"})
public class PersonResponse extends RepresentationModel<PersonResponse> {
    @JsonProperty("id")
    private final Long key;

    @JsonProperty("first_name")
    private final String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("address")
    private final String address;

    @JsonProperty("gender")
    private final String gender;

    public PersonResponse(Long key, String firstName, String lastName, String address, String gender) {
        this.key = key;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
    }

    public Long getKey() {
        return key;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonResponse that = (PersonResponse) o;
        return Objects.equals(key, that.key) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(address, that.address) && Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, firstName, lastName, address, gender);
    }
}
