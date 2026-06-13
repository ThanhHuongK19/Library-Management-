package project.librarymanagement.repository;

import project.librarymanagement.entity.Books;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import project.librarymanagement.entity.BorrowRecords.BorrowStatus;

@Repository
public interface BorrowRecordsRepository extends JpaRepository<BorrowRecords, Long> {

    List<BorrowRecords> findByUser(Users user);

    List<BorrowRecords> findByBook(Books book);

    List<BorrowRecords> findByStatus(BorrowStatus status);

    List<BorrowRecords> findByUserAndStatus(Users user, BorrowStatus status);

    List<BorrowRecords> findByBookAndStatus(Books book, BorrowStatus status);

    List<BorrowRecords> findByDueDateBeforeAndStatus(
            Timestamp dueDate,
            BorrowStatus status
    );

    List<BorrowRecords> findByBorrowDateBetween(
            Timestamp startDate,
            Timestamp endDate
    );
}