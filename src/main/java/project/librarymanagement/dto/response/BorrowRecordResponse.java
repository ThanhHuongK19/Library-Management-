package project.librarymanagement.dto.response;

import java.security.Timestamp;

public class BorrowRecordResponse {

    private Long id;

    private Long userId;
    private String username;

    private Long bookId;
    private String bookTitle;

    private Timestamp borrowDate;
    private Timestamp dueDate;
    private Timestamp returnDate;

    private String status;
}
