package project.librarymanagement.service.implementations;

import project.librarymanagement.entity.Books;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import project.librarymanagement.repository.BooksRepository;
import project.librarymanagement.service.interfaces.IBooksService;

import java.util.List;

@Service
public class BooksService implements IBooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    @Override
    public Books getBookById(long id) {
        return booksRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + id
                        )
                );
    }

    @Override
    public Books createBook(Books book) {
        if (book.getIsbn() != null
                && !book.getIsbn().isBlank()
                && booksRepository.existsByIsbn(book.getIsbn())) {
            throw new BadRequestException("ISBN already exists");
        }

        return booksRepository.save(book);
    }

    @Override
    public Books updateBook(long id, Books book) {
        Books existingBook = getBookById(id);

        if (book.getIsbn() != null
                && !book.getIsbn().isBlank()
                && !book.getIsbn().equals(existingBook.getIsbn())
                && booksRepository.existsByIsbn(book.getIsbn())) {
            throw new BadRequestException("ISBN already exists");
        }

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setPublishYear(book.getPublishYear());
        existingBook.setQuantity(book.getQuantity());
        existingBook.setSummary(book.getSummary());
        existingBook.setCategory(book.getCategory());

        return booksRepository.save(existingBook);
    }

    @Override
    public void deleteBook(long id) {
        Books book = getBookById(id);
        booksRepository.delete(book);
    }

    @Override
    public List<Books> searchByTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new BadRequestException("Title keyword must not be empty");
        }

        return booksRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Books> searchByAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new BadRequestException("Author keyword must not be empty");
        }

        return booksRepository.findByAuthorContainingIgnoreCase(author);
    }
}