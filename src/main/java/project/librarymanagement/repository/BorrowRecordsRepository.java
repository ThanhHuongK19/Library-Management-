package project.librarymanagement.repository;

import project.librarymanagement.entity.Books;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.entity.Users;
import project.librarymanagement.entity.BorrowRecords.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface BorrowRecordsRepository
        extends JpaRepository<BorrowRecords, Long> {

    // Get all borrow records of a user
    List<BorrowRecords> findBorrowRecordsByUser(
            Users user
    );

    // Get all borrow records of a book
    List<BorrowRecords> findBorrowRecordsByBook(
            Books book
    );

    // Get records by status
    List<BorrowRecords> findBorrowRecordsByStatus(
            BorrowStatus status
    );

    // Get records of a user by status
    List<BorrowRecords> findBorrowRecordsByUserAndStatus(
            Users user,
            BorrowStatus status
    );

    // Find overdue(qua han) records
    List<BorrowRecords> findBorrowRecordsByDueDateBeforeAndStatus(
            Timestamp dueDate,
            BorrowStatus status
    );

    // Get records between dates
    List<BorrowRecords> findBorrowRecordsByBorrowDateBetween(
            Timestamp startDate,
            Timestamp endDate
    );

    // su dung cho admin
    long countByStatus(
            BorrowStatus status
    );
}