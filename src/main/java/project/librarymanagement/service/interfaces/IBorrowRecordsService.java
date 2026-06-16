package project.librarymanagement.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.entity.BorrowRecords.BorrowStatus;

import java.security.Timestamp;
import java.util.List;

public interface IBorrowRecordsService {

    Page<BorrowRecords> getAllBorrowRecords(int page, int size);

    BorrowRecords getBorrowRecordById(Long borrowRecordId);

    BorrowRecords borrowBook(Long userId, Long bookId);

    BorrowRecords returnBook(Long borrowRecordId);

    Page<BorrowRecords> getBorrowRecordsByUser(Long userId, int page, int size);

//    Page<BorrowRecords> getBorrowRecordsByDateRange(Timestamp start, Timestamp end, int page, int size);
//
//    Page<BorrowRecords> getBorrowRecordsByStatus(BorrowStatus status, int page, int size);
//
//    Page<BorrowRecords> getBorrowRecordsByUserAndStatus(Long userId, BorrowStatus status, int page, int size);

    Page<BorrowRecords> getRecords(Specification<BorrowRecords> spec, int page, int size);

    void updateOverdueBorrowRecords();
}