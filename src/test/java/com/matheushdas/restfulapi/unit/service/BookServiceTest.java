package com.matheushdas.restfulapi.unit.service;

import com.matheushdas.restfulapi.dto.book.BookResponse;
import com.matheushdas.restfulapi.dto.book.CreateBookRequest;
import com.matheushdas.restfulapi.dto.book.UpdateBookRequest;
import com.matheushdas.restfulapi.exception.RequiredObjectIsNullException;
import com.matheushdas.restfulapi.exception.ResourceNotFoundException;
import com.matheushdas.restfulapi.mapper.BookMapper;
import com.matheushdas.restfulapi.model.Book;
import com.matheushdas.restfulapi.repository.BookRepository;
import com.matheushdas.restfulapi.service.BookService;
import com.matheushdas.restfulapi.unit.mock.BookMockProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private BookMockProvider input;

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        input = new BookMockProvider();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnBookResponseWithHateoasLink_whenFindById() {
        Book book = input.mockEntity(1);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(input.mockVO(1));

        BookResponse result = bookService.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/1>;rel=\"self\"]"));

        assertEquals(book.getId(), result.getKey());
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getLaunchDate(), result.getLaunchDate());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenFindByIdWithNoRecordedId() {
        when(bookRepository.findById(0L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.findById(0L);
        });
    }

    @Test
    void shouldCreateEntityAndSendBookResponseWithHateoasLink_whenSave() {
        CreateBookRequest request = input.mockCreateRequest(1);
        Book book = input.mockEntity(1);


        when(bookMapper.toEntity(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toResponse(book)).thenReturn(input.mockVO(1));

        BookResponse result = bookService.save(request);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/1>;rel=\"self\"]"));

        assertEquals(book.getId(), result.getKey());
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getLaunchDate(), result.getLaunchDate());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    void shouldThrowRequiredObjectIsNullException_whenCreateWithNull() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            bookService.save(null);
        });
    }

    @Test
    void shouldPatchEntityAndSendBookResponseWithHateoasLink_whenUpdate() {
        UpdateBookRequest request = input.mockUpdateRequest(1);
        Book book = input.mockEntity(1);


        when(bookRepository.findById(request.id())).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toResponse(book)).thenReturn(input.mockVO(1));

        BookResponse result = bookService.update(request);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/1>;rel=\"self\"]"));

        assertEquals(book.getId(), result.getKey());
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getLaunchDate(), result.getLaunchDate());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    void shouldThrowRequiredObjectIsNullException_whenUpdateWithNull() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            bookService.update(null);
        });
    }
}
