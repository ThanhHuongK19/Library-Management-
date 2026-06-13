package project.librarymanagement.service.implementations;

import project.librarymanagement.entity.Books;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.entity.BorrowRecords.BorrowStatus;
import project.librarymanagement.entity.Users;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.librarymanagement.repository.BooksRepository;
import project.librarymanagement.repository.BorrowRecordsRepository;
import project.librarymanagement.repository.UsersRepository;
import project.librarymanagement.service.interfaces.IBorrowRecordsService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowRecordsService implements IBorrowRecordsService {

    private final BorrowRecordsRepository borrowRecordsRepository;
    private final UsersRepository usersRepository;
    private final BooksRepository booksRepository;

    public BorrowRecordsService(
            BorrowRecordsRepository borrowRecordsRepository,
            UsersRepository usersRepository,
            BooksRepository booksRepository) {

        this.borrowRecordsRepository = borrowRecordsRepository;
        this.usersRepository = usersRepository;
        this.booksRepository = booksRepository;
    }

    @Override
    public List<BorrowRecords> getAllBorrowRecords() {
        return borrowRecordsRepository.findAll();
    }

    @Override
    public BorrowRecords getBorrowRecordById(long id) {
        return borrowRecordsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Borrow record not found with id: " + id
                        )
                );
    }

    @Override
    @Transactional
    public BorrowRecords borrowBook(long userId, long bookId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        Books book = booksRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + bookId
                        )
                );

        if (book.getQuantity() <= 0) {
            throw new BadRequestException("Book is out of stock");
        }

        book.setQuantity(book.getQuantity() - 1);

        BorrowRecords record = new BorrowRecords();
        record.setUser(user);
        record.setBook(book);
        record.setBorrowDate(Timestamp.valueOf(LocalDateTime.now()));
        record.setDueDate(Timestamp.valueOf(LocalDateTime.now().plusDays(14)));
        record.setReturnDate(null);
        record.setStatus(BorrowStatus.BORROWED);

        booksRepository.save(book);

        return borrowRecordsRepository.save(record);
    }

    @Override
    @Transactional
    public BorrowRecords returnBook(long borrowRecordId) {

        BorrowRecords record = borrowRecordsRepository.findById(borrowRecordId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Borrow record not found with id: "
                                        + borrowRecordId
                        )
                );

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new BadRequestException("Book already returned");
        }

        Books book = record.getBook();

        if (book == null) {
            throw new ResourceNotFoundException(
                    "Book information not found in borrow record"
            );
        }

        book.setQuantity(book.getQuantity() + 1);

        record.setReturnDate(Timestamp.valueOf(LocalDateTime.now()));
        record.setStatus(BorrowStatus.RETURNED);

        booksRepository.save(book);

        return borrowRecordsRepository.save(record);
    }

    @Override
    public List<BorrowRecords> getBorrowRecordsByUser(long userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        return borrowRecordsRepository.findByUser(user);
    }

    @Override
    public List<BorrowRecords> getBorrowRecordsByBook(long bookId) {

        Books book = booksRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + bookId
                        )
                );

        return borrowRecordsRepository.findByBook(book);
    }
}