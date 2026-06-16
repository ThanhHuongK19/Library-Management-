package project.librarymanagement.service.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.librarymanagement.entity.Books;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import project.librarymanagement.repository.BooksRepository;
import project.librarymanagement.repository.BorrowRecordsRepository;
import project.librarymanagement.repository.UsersRepository;
import project.librarymanagement.service.interfaces.IBorrowRecordsService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static project.librarymanagement.entity.BorrowRecords.BorrowStatus.*;

@Service
public class BorrowRecordsService implements IBorrowRecordsService {

    private final BorrowRecordsRepository borrowRecordsRepository;
    private final UsersRepository usersRepository;
    private final BooksRepository booksRepository;

    public BorrowRecordsService(BorrowRecordsRepository borrowRecordsRepository,
                                UsersRepository usersRepository,
                                BooksRepository booksRepository) {
        this.borrowRecordsRepository = borrowRecordsRepository;
        this.usersRepository = usersRepository;
        this.booksRepository = booksRepository;
    }

    @Override
    public Page<BorrowRecords> getAllBorrowRecords(int page, int size) {
        return borrowRecordsRepository.findAll(PageRequest.of(page, size, Sort.by("borrowDate").descending()));
    }

    @Override
    public BorrowRecords getBorrowRecordById(Long id) {
        return borrowRecordsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found: " + id));
    }

    @Override
    @Transactional
    public BorrowRecords borrowBook(Long userId, Long bookId) {
        var user = usersRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        var book = booksRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getQuantity() == null || book.getQuantity() <= 0) throw new BadRequestException("Book unavailable");

        BorrowRecords record = new BorrowRecords();
        record.setUser(user);
        record.setBook(book);
        record.setBorrowDate(Timestamp.valueOf(LocalDateTime.now()));
        record.setDueDate(Timestamp.valueOf(LocalDateTime.now().plusDays(14)));
        record.setStatus(BORROWED);

        book.setQuantity(book.getQuantity() - 1);
        booksRepository.save(book);
        return borrowRecordsRepository.save(record);
    }

    @Override
    @Transactional
    public BorrowRecords returnBook(Long id) {
        BorrowRecords record = getBorrowRecordById(id);
        if (record.getStatus() == RETURNED) throw new BadRequestException("Already returned");

        Books book = record.getBook();
        book.setQuantity(book.getQuantity() + 1);
        record.setReturnDate(Timestamp.valueOf(LocalDateTime.now()));
        record.setStatus(RETURNED);

        booksRepository.save(book);
        return borrowRecordsRepository.save(record);
    }

    @Override
    @Transactional
    public void updateOverdueBorrowRecords() {
        var records = borrowRecordsRepository.findBorrowRecordsByDueDateBeforeAndStatus(
                Timestamp.valueOf(LocalDateTime.now()), BORROWED, Pageable.unpaged());
        records.forEach(r -> { r.setStatus(OVERDUE); r.setNotes("Book is overdue"); });
        borrowRecordsRepository.saveAll(records);
    }

    @Override
    public Page<BorrowRecords> getRecords(Specification<BorrowRecords> spec, int page, int size) {
        return borrowRecordsRepository.findAll(spec, PageRequest.of(page, size, Sort.by("borrowDate").descending()));
    }

    @Override
    public Page<BorrowRecords> getBorrowRecordsByUser(Long userId, int page, int size) {
        Specification<BorrowRecords> spec = (root, query, cb) -> cb.equal(root.get("user").get("userId"), userId);
        return getRecords(spec, page, size);
    }
}