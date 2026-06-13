package project.librarymanagement.service.implementations;

import project.librarymanagement.dto.request.CreateBookRequest;
import project.librarymanagement.dto.request.UpdateBookRequest;
import project.librarymanagement.entity.Books;
import project.librarymanagement.entity.Categories;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import project.librarymanagement.repository.BooksRepository;
import project.librarymanagement.repository.CategoriesRepository;
import project.librarymanagement.service.interfaces.IBooksService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BooksService implements IBooksService {

    private final BooksRepository booksRepository;
    private final CategoriesRepository categoriesRepository;

    public BooksService(
            BooksRepository booksRepository,
            CategoriesRepository categoriesRepository
    ) {
        this.booksRepository = booksRepository;
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public Page<Books> getAllBooks(
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return booksRepository.findAll(pageable);
    }

    @Override
    public Books getBookById(Long bookId) {
        return booksRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + bookId
                        )
                );
    }

    @Override
    @Transactional
    public Books createBook(CreateBookRequest request) {

        if (request.getIsbn() != null
                && booksRepository.existsBookByIsbn(request.getIsbn())) {
            throw new BadRequestException(
                    "ISBN already exists: " + request.getIsbn()
            );
        }

        Categories category = categoriesRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: "
                                        + request.getCategoryId()
                        )
                );

        Books book = new Books();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublisher(request.getPublisher());
        book.setPublishYear(request.getPublishYear());
        book.setQuantity(request.getQuantity());
        book.setSummary(request.getSummary());
        book.setCategory(category);

        return booksRepository.save(book);
    }

    @Override
    @Transactional
    public Books updateBook(
            Long bookId,
            UpdateBookRequest request
    ) {
        Books existingBook = getBookById(bookId);

        if (request.getIsbn() != null
                && !request.getIsbn().equals(existingBook.getIsbn())
                && booksRepository.existsBookByIsbn(request.getIsbn())) {
            throw new BadRequestException(
                    "ISBN already exists: " + request.getIsbn()
            );
        }

        Categories category = categoriesRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: "
                                        + request.getCategoryId()
                        )
                );

        existingBook.setTitle(request.getTitle());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setIsbn(request.getIsbn());
        existingBook.setPublisher(request.getPublisher());
        existingBook.setPublishYear(request.getPublishYear());
        existingBook.setQuantity(request.getQuantity());
        existingBook.setSummary(request.getSummary());
        existingBook.setCategory(category);

        return booksRepository.save(existingBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Books book = getBookById(bookId);
        booksRepository.delete(book);
    }

    @Override
    public Books getBookByIsbn(String isbn) {
        return booksRepository.findBookByIsbn(isbn)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with ISBN: " + isbn
                        )
                );
    }

    @Override
    public Page<Books> searchBooksByKeyword(
            String keyword,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return booksRepository
                .findBooksByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        keyword,
                        keyword,
                        pageable
                );
    }

    @Override
    public Page<Books> searchBooksByTitle(
            String title,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return booksRepository.findBooksByTitleContainingIgnoreCase(
                title,
                pageable
        );
    }

    @Override
    public Page<Books> searchBooksByAuthor(
            String author,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return booksRepository.findBooksByAuthorContainingIgnoreCase(
                author,
                pageable
        );
    }

    @Override
    public Page<Books> findBooksByCategory(
            Categories category,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return booksRepository.findBooksByCategory(
                category,
                pageable
        );
    }

    @Override
    public Page<Books> findAvailableBooks(
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return booksRepository.findBooksByQuantityGreaterThan(
                0,
                pageable
        );
    }
}