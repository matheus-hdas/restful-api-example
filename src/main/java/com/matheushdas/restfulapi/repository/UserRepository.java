package com.matheushdas.restfulapi.repository;

import com.matheushdas.restfulapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT t FROM User t WHERE t.email = ?1 OR t.username = ?1")
    User findByUsernameOrEmail(String username);
}
