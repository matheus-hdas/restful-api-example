package com.matheushdas.restfulapi.controller;

import com.matheushdas.restfulapi.dto.book.BookResponse;
import com.matheushdas.restfulapi.dto.book.CreateBookRequest;
import com.matheushdas.restfulapi.dto.book.UpdateBookRequest;
import com.matheushdas.restfulapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.matheushdas.restfulapi.util.MediaType.*;

@RestController
@RequestMapping("/api/book")
@Tag(name = "Book", description = "Endpoints to managing books")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(produces = { JSON, XML, YML })
    @Operation(
            summary = "Finds All Books",
            description = "Return all books list",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = { @Content(
                                    mediaType = JSON,
                                    array = @ArraySchema(schema = @Schema(
                                            implementation = BookResponse.class)
                                    )
                            ) }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping(value = "/{id}", produces = { JSON, XML, YML })
    @Operation(
            summary = "Find Book By Id",
            description = "Search on DB a book with provided id",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = { @Content(
                                    schema = @Schema(implementation = BookResponse.class)
                            ) }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping(produces = { JSON, XML, YML }, consumes = { JSON, XML, YML })
    @Operation(
            summary = "Create a new Book",
            description = "Save a Book on DB",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = { @Content(
                                    schema = @Schema(implementation = BookResponse.class)
                            ) }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid CreateBookRequest book) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.save(book));
    }

    @PutMapping(produces = { JSON, XML, YML }, consumes = { JSON, XML, YML })
    @Operation(
            summary = "Updates a Book",
            description = "Patch any Book field",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = { @Content(
                                    schema = @Schema(implementation = BookResponse.class)
                            ) }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<BookResponse> updateBook(@RequestBody @Valid UpdateBookRequest book) {
        return ResponseEntity.ok(bookService.update(book));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletes a Book",
            description = "Remove a Book by provided id",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "No Content",
                            responseCode = "204"
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
