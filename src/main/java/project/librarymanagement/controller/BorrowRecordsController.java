package project.librarymanagement.controller;

import org.springframework.data.domain.Page;
import project.librarymanagement.dto.response.BorrowRecordResponse;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.service.interfaces.IBorrowRecordsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/borrow-records")
public class BorrowRecordsController {

    private final IBorrowRecordsService borrowRecordsService;

    public BorrowRecordsController(IBorrowRecordsService borrowRecordsService) {
        this.borrowRecordsService = borrowRecordsService;
    }

    @GetMapping
    public ResponseEntity<Page<BorrowRecordResponse>> getAllBorrowRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Gọi service đã phân trang
        Page<BorrowRecordResponse> responsePage = borrowRecordsService.getAllBorrowRecords(page, size)
                .map(this::toBorrowRecordResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BorrowRecordResponse>> getBorrowRecordsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Gọi service tìm theo user với phân trang
        Page<BorrowRecordResponse> responsePage = borrowRecordsService.getBorrowRecordsByUser(userId, page, size)
                .map(this::toBorrowRecordResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRecordResponse> getBorrowRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(toBorrowRecordResponse(borrowRecordsService.getBorrowRecordById(id)));
    }

    @PostMapping("/borrow")
    public ResponseEntity<Void> borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        borrowRecordsService.borrowBook(userId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable Long id) {
        borrowRecordsService.returnBook(id);
        return ResponseEntity.noContent().build();
    }

    private BorrowRecordResponse toBorrowRecordResponse(BorrowRecords record) {
        Long uId = record.getUser() != null ? record.getUser().getUserId() : null;
        String uName = record.getUser() != null ? record.getUser().getUsername() : null;
        Long bId = record.getBook() != null ? record.getBook().getBookId() : null;
        String bTitle = record.getBook() != null ? record.getBook().getTitle() : null;

        return new BorrowRecordResponse(
                record.getId(), uId, uName, bId, bTitle,
                record.getBorrowDate(), record.getDueDate(), record.getReturnDate(), record.getStatus()
        );
    }
}