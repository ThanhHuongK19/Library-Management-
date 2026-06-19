package project.librarymanagement.controller;

import project.librarymanagement.dto.request.CreateBookRequest;
import project.librarymanagement.dto.request.UpdateBookRequest;
import project.librarymanagement.dto.response.BookResponse;
import project.librarymanagement.entity.Books;
import project.librarymanagement.mapper.BookMapper;
import project.librarymanagement.service.interfaces.IBooksService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BooksController {

    private final IBooksService booksService;
    private final BookMapper bookMapper;

    public BooksController(IBooksService booksService, BookMapper bookMapper) {
        this.booksService = booksService;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(
                booksService.getAllBooks(page, size, sortBy, sortDir)
                        .map(bookMapper::toBookResponse)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                bookMapper.toBookResponse(booksService.getBookById(id))
        );
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(
            @PathVariable String isbn
    ) {
        return ResponseEntity.ok(
                bookMapper.toBookResponse(booksService.getBookByIsbn(isbn))
        );
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody CreateBookRequest request
    ) {
        Books createdBook = booksService.createBook(request);

        return new ResponseEntity<>(
                bookMapper.toBookResponse(createdBook),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookRequest request
    ) {
        return ResponseEntity.ok(
                bookMapper.toBookResponse(
                        booksService.updateBook(id, request)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id
    ) {
        booksService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                booksService.searchBooksByKeyword(keyword, page, size)
                        .map(bookMapper::toBookResponse)
        );
    }

    @GetMapping("/available")
    public ResponseEntity<Page<BookResponse>> getAvailableBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                booksService.findAvailableBooks(page, size)
                        .map(bookMapper::toBookResponse)
        );
    }
}