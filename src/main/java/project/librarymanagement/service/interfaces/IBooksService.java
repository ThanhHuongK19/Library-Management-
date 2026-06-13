package project.librarymanagement.service.interfaces;

import project.librarymanagement.dto.request.CreateBookRequest;
import project.librarymanagement.dto.request.UpdateBookRequest;
import project.librarymanagement.entity.Books;
import project.librarymanagement.entity.Categories;
import org.springframework.data.domain.Page;

public interface IBooksService {

    Page<Books> getAllBooks(
            int page,
            int size,
            String sortBy,
            String sortDir
    );

    Books getBookById(Long bookId);

    Books createBook(CreateBookRequest request);

    Books updateBook(Long bookId, UpdateBookRequest request);

    void deleteBook(Long bookId);

    Books getBookByIsbn(String isbn);

    Page<Books> searchBooksByKeyword(
            String keyword,
            int page,
            int size
    );

    Page<Books> searchBooksByTitle(
            String title,
            int page,
            int size
    );

    Page<Books> searchBooksByAuthor(
            String author,
            int page,
            int size
    );

    Page<Books> findBooksByCategory(
            Categories category,
            int page,
            int size
    );

    Page<Books> findAvailableBooks(
            int page,
            int size
    );
}