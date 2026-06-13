package project.librarymanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.librarymanagement.entity.BorrowRecords.BorrowStatus;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class BorrowRecordResponse {

    private Long id;

    private Long userId;
    private String username;

    private Long bookId;
    private String bookTitle;

    private Timestamp borrowDate;
    private Timestamp dueDate;
    private Timestamp returnDate;

    private BorrowStatus status;
}