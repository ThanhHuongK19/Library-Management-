package project.librarymanagement.controller;

import project.librarymanagement.entity.Books;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.librarymanagement.service.interfaces.IBooksService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BooksController {
    private final IBooksService booksService;

    public BooksController(IBooksService booksService){
        this.booksService = booksService;
    }

    @GetMapping("/get-all-books")
    public ResponseEntity<List<Books>> getAllBooks(){
        return ResponseEntity.ok(booksService.getAllBooks());
    }

    @GetMapping("/get-book/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable long id) {
        return ResponseEntity.ok(booksService.getBookById(id));
    }

    @PostMapping("/create-book")
    public ResponseEntity<Books> createBook(@Valid @RequestBody Books book){
        return new ResponseEntity<>(
                booksService.createBook(book),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/update-book/{id}")
    public ResponseEntity<Books> updateBook(
            @PathVariable long id,
            @Valid @RequestBody Books book) {

        return ResponseEntity.ok(booksService.updateBook(id, book));
    }

    @DeleteMapping("/delete-book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        booksService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search-book/title")
    public ResponseEntity<List<Books>> searchByTitle(
            @RequestParam String title) {

        return ResponseEntity.ok(booksService.searchByTitle(title));
    }

    @GetMapping("/search-book/author")
    public ResponseEntity<List<Books>> searchByAuthor(
            @RequestParam String author) {

        return ResponseEntity.ok(booksService.searchByAuthor(author));
    }
}
