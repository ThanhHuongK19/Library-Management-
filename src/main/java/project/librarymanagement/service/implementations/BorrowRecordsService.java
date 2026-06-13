package project.librarymanagement.service.implementations;

import project.librarymanagement.entity.Books;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.entity.Users;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import project.librarymanagement.repository.BooksRepository;
import project.librarymanagement.repository.BorrowRecordsRepository;
import project.librarymanagement.repository.UsersRepository;
import project.librarymanagement.service.interfaces.IBorrowRecordsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static project.librarymanagement.entity.BorrowRecords.BorrowStatus.BORROWED;
import static project.librarymanagement.entity.BorrowRecords.BorrowStatus.OVERDUE;
import static project.librarymanagement.entity.BorrowRecords.BorrowStatus.RETURNED;

@Service
public class BorrowRecordsService implements IBorrowRecordsService {

    private final BorrowRecordsRepository borrowRecordsRepository;
    private final UsersRepository usersRepository;
    private final BooksRepository booksRepository;

    public BorrowRecordsService(
            BorrowRecordsRepository borrowRecordsRepository,
            UsersRepository usersRepository,
            BooksRepository booksRepository
    ) {
        this.borrowRecordsRepository = borrowRecordsRepository;
        this.usersRepository = usersRepository;
        this.booksRepository = booksRepository;
    }

    @Override
    public List<BorrowRecords> getAllBorrowRecords() {
        return borrowRecordsRepository.findAll();
    }

    @Override
    public BorrowRecords getBorrowRecordById(Long borrowRecordId) {
        return borrowRecordsRepository.findById(borrowRecordId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Borrow record not found with id: " + borrowRecordId
                        )
                );
    }

    @Override
    @Transactional
    public BorrowRecords borrowBook(Long userId, Long bookId) {
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

        if (book.getQuantity() == null || book.getQuantity() <= 0) {
            throw new BadRequestException(
                    "Book is not available for borrowing"
            );
        }

        BorrowRecords borrowRecord = new BorrowRecords();
        borrowRecord.setUser(user);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(
                Timestamp.valueOf(LocalDateTime.now())
        );
        borrowRecord.setDueDate(
                Timestamp.valueOf(LocalDateTime.now().plusDays(14))
        );
        borrowRecord.setStatus(BORROWED);
        borrowRecord.setNotes("Book borrowed successfully");

        book.setQuantity(book.getQuantity() - 1);
        booksRepository.save(book);

        return borrowRecordsRepository.save(borrowRecord);
    }

    @Override
    @Transactional
    public BorrowRecords returnBook(Long borrowRecordId) {
        BorrowRecords borrowRecord = getBorrowRecordById(borrowRecordId);

        if (borrowRecord.getStatus() == RETURNED) {
            throw new BadRequestException(
                    "This book has already been returned"
            );
        }

        Books book = borrowRecord.getBook();
        book.setQuantity(book.getQuantity() + 1);

        borrowRecord.setReturnDate(
                Timestamp.valueOf(LocalDateTime.now())
        );
        borrowRecord.setStatus(RETURNED);
        borrowRecord.setNotes("Book returned successfully");

        booksRepository.save(book);
        return borrowRecordsRepository.save(borrowRecord);
    }

    @Override
    public List<BorrowRecords> getBorrowRecordsByUser(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        return borrowRecordsRepository.findBorrowRecordsByUser(user);
    }

    @Override
    public List<BorrowRecords> getBorrowRecordsByBook(Long bookId) {
        Books book = booksRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + bookId
                        )
                );

        return borrowRecordsRepository.findBorrowRecordsByBook(book);
    }

    @Override
    public List<BorrowRecords> getBorrowRecordsByStatus(
            BorrowRecords.BorrowStatus status
    ) {
        return borrowRecordsRepository.findBorrowRecordsByStatus(status);
    }

    @Override
    public List<BorrowRecords> getBorrowRecordsByUserAndStatus(
            Long userId,
            BorrowRecords.BorrowStatus status
    ) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        return borrowRecordsRepository.findBorrowRecordsByUserAndStatus(
                user,
                status
        );
    }

    @Override
    public List<BorrowRecords> getOverdueBorrowRecords() {
        return borrowRecordsRepository
                .findBorrowRecordsByDueDateBeforeAndStatus(
                        Timestamp.valueOf(LocalDateTime.now()),
                        BORROWED
                );
    }

    @Override
    @Transactional
    public void updateOverdueBorrowRecords() {
        List<BorrowRecords> overdueRecords = getOverdueBorrowRecords();

        for (BorrowRecords record : overdueRecords) {
            record.setStatus(OVERDUE);
            record.setNotes("Book is overdue");
        }

        borrowRecordsRepository.saveAll(overdueRecords);
    }
}