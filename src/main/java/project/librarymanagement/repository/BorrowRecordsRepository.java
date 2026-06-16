package project.librarymanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
        extends JpaRepository<BorrowRecords, Long>,
        JpaSpecificationExecutor<BorrowRecords> {

    Page<BorrowRecords> findBorrowRecordsByUser(Users user, Pageable pageable);

    Page<BorrowRecords> findBorrowRecordsByBook(Books book, Pageable pageable);

    Page<BorrowRecords> findBorrowRecordsByStatus(BorrowStatus status, Pageable pageable);

    Page<BorrowRecords> findBorrowRecordsByUserAndStatus(Users user, BorrowStatus status, Pageable pageable);

    Page<BorrowRecords> findBorrowRecordsByDueDateBeforeAndStatus(Timestamp dueDate, BorrowStatus status, Pageable pageable);

    Page<BorrowRecords> findByBorrowDateBetween(Timestamp start, Timestamp end, Pageable pageable);

    // su dung cho admin
    long countByStatus(
            BorrowStatus status
    );

    // su dung cho qua han
    long countByStatusAndDueDateBefore(BorrowStatus status, Timestamp date);
}