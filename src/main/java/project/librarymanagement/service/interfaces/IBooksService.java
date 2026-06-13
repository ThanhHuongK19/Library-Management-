package project.librarymanagement.service.interfaces;

import project.librarymanagement.entity.Books;
import java.util.List;

public interface IBooksService {
    List<Books> getAllBooks();

    Books getBookById(long id);

    Books createBook(Books book);

    Books updateBook(long id, Books book);

    void deleteBook(long id);

    List<Books> searchByTitle(String title);

    List<Books> searchByAuthor(String author);
}
