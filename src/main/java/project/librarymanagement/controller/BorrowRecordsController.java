package project.librarymanagement.controller;

import project.librarymanagement.dto.response.BorrowRecordResponse;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.entity.BorrowRecords.BorrowStatus;
import project.librarymanagement.service.interfaces.IBorrowRecordsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/borrow-records")
public class BorrowRecordsController {

    private final IBorrowRecordsService borrowRecordsService;

    public BorrowRecordsController(IBorrowRecordsService borrowRecordsService) {
        this.borrowRecordsService = borrowRecordsService;
    }

    @GetMapping
    public ResponseEntity<List<BorrowRecordResponse>> getAllBorrowRecords() {
        return ResponseEntity.ok(
                borrowRecordsService.getAllBorrowRecords()
                        .stream()
                        .map(this::toBorrowRecordResponse)
                        .collect(Collectors.toList())
        );
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowRecordResponse>> getBorrowRecordsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByUser(userId)
                        .stream()
                        .map(this::toBorrowRecordResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BorrowRecordResponse>> getBorrowRecordsByStatus(@PathVariable BorrowStatus status) {
        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByStatus(status)
                        .stream()
                        .map(this::toBorrowRecordResponse)
                        .collect(Collectors.toList())
        );
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