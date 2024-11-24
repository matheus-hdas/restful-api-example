package com.matheushdas.restfulapi.mapper;

import com.matheushdas.restfulapi.dto.book.BookResponse;
import com.matheushdas.restfulapi.dto.book.CreateBookRequest;
import com.matheushdas.restfulapi.dto.book.UpdateBookRequest;
import com.matheushdas.restfulapi.model.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookMapper {
    public BookResponse toResponse(Book data) {
        return new BookResponse(
                data.getId(),
                data.getTitle(),
                data.getAuthor(),
                data.getLaunchDate(),
                data.getPrice()
        );
    }

    public List<BookResponse> toResponseList(List<Book> data) {
        List<BookResponse> result = new ArrayList<>();

        for(Book b : data) {
            result.add(this.toResponse(b));
        }
        return result;
    }

    public Book toEntity(CreateBookRequest data) {
        return new Book(
                data.title(),
                data.author(),
                data.launchDate(),
                data.price()
        );
    }

    public Book toEntity(UpdateBookRequest data) {
        return new Book(
                data.id(),
                data.title(),
                data.author(),
                data.launchDate(),
                data.price()
        );
    }
}
