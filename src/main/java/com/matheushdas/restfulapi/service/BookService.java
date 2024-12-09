package com.matheushdas.restfulapi.service;

import com.matheushdas.restfulapi.controller.BookController;
import com.matheushdas.restfulapi.dto.book.BookResponse;
import com.matheushdas.restfulapi.dto.book.CreateBookRequest;
import com.matheushdas.restfulapi.dto.book.UpdateBookRequest;
import com.matheushdas.restfulapi.exception.RequiredObjectIsNullException;
import com.matheushdas.restfulapi.exception.ResourceNotFoundException;
import com.matheushdas.restfulapi.mapper.BookMapper;
import com.matheushdas.restfulapi.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {
    private BookRepository bookRepository;
    private BookMapper bookMapper;
    private PagedResourcesAssembler<BookResponse> resourcesAssembler;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, PagedResourcesAssembler<BookResponse> resourcesAssembler) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.resourcesAssembler = resourcesAssembler;
    }

    public PagedModel<EntityModel<BookResponse>> findAll(Pageable pageable) {
        Page page = bookMapper
                .toResponsePage(bookRepository.findAll(pageable))
                .map(p -> p.add(
                        linkTo(
                                methodOn(BookController.class)
                                        .getBookById(p.getKey()))
                                .withSelfRel()));

        Link link = linkTo(
                methodOn(BookController.class)
                        .getAllBooks(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc"))
                .withSelfRel();
        return resourcesAssembler.toModel(page, link);
    }

    public PagedModel<EntityModel<BookResponse>> findByTitle(String title, Pageable pageable) {
        Page page = bookMapper
                .toResponsePage(bookRepository.findByTitle(title, pageable))
                .map(p -> p.add(
                        linkTo(
                                methodOn(BookController.class)
                                        .getBookById(p.getKey()))
                                .withSelfRel()));

        Link link = linkTo(
                methodOn(BookController.class)
                        .getBooksByTitle(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc",
                                title))
                .withSelfRel();
        return resourcesAssembler.toModel(page, link);
    }

    public BookResponse findById(Long id) {
        BookResponse result =
                bookMapper.toResponse(bookRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")));
        result.add(
                linkTo(
                        methodOn(BookController.class)
                                .getBookById(result.getKey()))
                        .withSelfRel());
        return result;
    }

    public BookResponse save(CreateBookRequest book) {
        if(book == null) throw new RequiredObjectIsNullException("Book cannot be null!");
        BookResponse result = bookMapper.toResponse(
                bookRepository.save(bookMapper.toEntity(book)));
        result.add(
                linkTo(
                        methodOn(BookController.class)
                                .getBookById(result.getKey()))
                        .withSelfRel());
        return result;
    }

    public BookResponse update(UpdateBookRequest book) {
        if(book == null) throw new RequiredObjectIsNullException("Book cannot be null!");

        BookResponse result = bookMapper.toResponse(
             bookRepository.findById(book.id())
                     .map(toUpdate -> {
                         toUpdate.setTitle(book.title());
                         toUpdate.setAuthor(book.author());
                         toUpdate.setLaunchDate(book.launchDate());
                         toUpdate.setPrice(book.price());
                         return bookRepository.save(toUpdate);
                     })
                     .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"))
        );

        result.add(
                linkTo(
                        methodOn(BookController.class)
                                .getBookById(result.getKey()))
                        .withSelfRel());
        return result;
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
