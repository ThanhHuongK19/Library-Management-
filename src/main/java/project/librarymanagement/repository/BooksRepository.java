package project.librarymanagement.repository;

import project.librarymanagement.entity.Books;
import project.librarymanagement.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

    // Find book by ISBN
    Optional<Books> findBookByIsbn(String isbn);

    // Check ISBN already exists
    boolean existsBookByIsbn(String isbn);

    // Check category contains books
    boolean existsBooksByCategory(Categories category);

    // Search books by title
    Page<Books> findBooksByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );

    // Search books by author
    Page<Books> findBooksByAuthorContainingIgnoreCase(
            String author,
            Pageable pageable
    );

    // Find books by category
    Page<Books> findBooksByCategory(
            Categories category,
            Pageable pageable
    );

    // Find available books
    Page<Books> findBooksByQuantityGreaterThan(
            Integer quantity,
            Pageable pageable
    );

    // Search books by title or author
    Page<Books> findBooksByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String author,
            Pageable pageable
    );
}