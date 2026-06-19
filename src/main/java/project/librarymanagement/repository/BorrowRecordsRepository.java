package project.librarymanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.entity.BorrowRecords.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface BorrowRecordsRepository
        extends JpaRepository<BorrowRecords, Long>,
        JpaSpecificationExecutor<BorrowRecords> {

    Page<BorrowRecords> findBorrowRecordsByDueDateBeforeAndStatus(Timestamp dueDate, BorrowStatus status, Pageable pageable);

    // su dung cho admin
    long countByStatus(BorrowStatus status);

    // su dung cho qua han
    long countByStatusAndDueDateBefore(BorrowStatus status, Timestamp date);
}