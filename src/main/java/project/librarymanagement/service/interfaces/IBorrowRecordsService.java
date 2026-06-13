package project.librarymanagement.service.interfaces;

import project.librarymanagement.entity.BorrowRecords;

import java.util.List;

public interface IBorrowRecordsService {
    List<BorrowRecords> getAllBorrowRecords();

    BorrowRecords getBorrowRecordById(long id);

    BorrowRecords borrowBook(long userId, long bookId);

    BorrowRecords returnBook(long borrowRecordId);

    List<BorrowRecords> getBorrowRecordsByUser(long userId);

    List<BorrowRecords> getBorrowRecordsByBook(long bookId);
}
