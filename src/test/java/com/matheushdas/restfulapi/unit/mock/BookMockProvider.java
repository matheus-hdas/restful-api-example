package com.matheushdas.restfulapi.unit.mock;

import com.matheushdas.restfulapi.dto.book.BookResponse;
import com.matheushdas.restfulapi.dto.book.CreateBookRequest;
import com.matheushdas.restfulapi.dto.book.UpdateBookRequest;
import com.matheushdas.restfulapi.dto.person.UpdatePersonRequest;
import com.matheushdas.restfulapi.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookMockProvider {
    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookResponse mockVO() {
        return mockVO(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookResponse> mockVOList() {
        List<BookResponse> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }

    public Book mockEntity(Integer number) {
        return new Book(
                number.longValue(),
                "Title Test" + number,
                "AuthorTest" + number,
                new Date(2000, 10, number + 1),
                number.doubleValue() * 10
        );
    }

    public CreateBookRequest mockCreateRequest(Integer number) {
        return new CreateBookRequest(
                "Title Test" + number,
                "AuthorTest" + number,
                new Date(2000, 10, number + 1),
                number.doubleValue() * 10
        );
    }

    public UpdateBookRequest mockUpdateRequest(Integer number) {
        return new UpdateBookRequest(
                number.longValue(),
                "Title Test" + number,
                "AuthorTest" + number,
                new Date(2000, 10, number + 1),
                number.doubleValue() * 10
        );
    }

    public BookResponse mockVO(Integer number) {
        return new BookResponse(
                number.longValue(),
                "Title Test" + number,
                "AuthorTest" + number,
                new Date(2000, 10, number + 1),
                number.doubleValue() * 10
        );
    }
}
