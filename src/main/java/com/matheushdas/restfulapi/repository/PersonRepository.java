package com.matheushdas.restfulapi.repository;

import com.matheushdas.restfulapi.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Modifying
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id = ?1")
    void disablePerson(Long id);

    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT ('%',?1,'%'))")
    Page<Person> findByName(String name, Pageable pageable);
}
