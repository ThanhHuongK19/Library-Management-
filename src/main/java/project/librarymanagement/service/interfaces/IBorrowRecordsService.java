package project.librarymanagement.service.interfaces;

import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.entity.BorrowRecords.BorrowStatus;

import java.util.List;

public interface IBorrowRecordsService {

    List<BorrowRecords> getAllBorrowRecords();

    BorrowRecords getBorrowRecordById(Long borrowRecordId);

    BorrowRecords borrowBook(
            Long userId,
            Long bookId
    );

    BorrowRecords returnBook(
            Long borrowRecordId
    );

    List<BorrowRecords> getBorrowRecordsByUser(
            Long userId
    );

    List<BorrowRecords> getBorrowRecordsByBook(
            Long bookId
    );

    List<BorrowRecords> getBorrowRecordsByStatus(
            BorrowStatus status
    );

    List<BorrowRecords> getBorrowRecordsByUserAndStatus(
            Long userId,
            BorrowStatus status
    );

    List<BorrowRecords> getOverdueBorrowRecords();

    void updateOverdueBorrowRecords();
}