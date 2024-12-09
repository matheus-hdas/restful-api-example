package com.matheushdas.restfulapi.repository;

import com.matheushdas.restfulapi.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.title LIKE LOWER(CONCAT ('%',?1,'%'))")
    Page<Book> findByTitle(String title, Pageable pageable);
}
