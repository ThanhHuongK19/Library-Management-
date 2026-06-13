package project.librarymanagement.repository;

import project.librarymanagement.entity.Books;
import project.librarymanagement.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

    Optional<Books> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    boolean existsByCategory(Categories category);

    List<Books> findByTitleContainingIgnoreCase(String title);

    List<Books> findByAuthorContainingIgnoreCase(String author);

    List<Books> findByCategory(Categories category);

    List<Books> findByQuantityGreaterThan(int quantity);
}