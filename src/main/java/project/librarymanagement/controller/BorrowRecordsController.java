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

    public BorrowRecordsController(
            IBorrowRecordsService borrowRecordsService
    ) {
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
    public ResponseEntity<BorrowRecordResponse> getBorrowRecordById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                toBorrowRecordResponse(
                        borrowRecordsService.getBorrowRecordById(id)
                )
        );
    }

    // Mượn sách
    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecordResponse> borrowBook(
            @RequestParam Long userId,
            @RequestParam Long bookId
    ) {
        BorrowRecords borrowRecord =
                borrowRecordsService.borrowBook(userId, bookId);

        return new ResponseEntity<>(
                toBorrowRecordResponse(borrowRecord),
                HttpStatus.CREATED
        );
    }

    // update status borrowed --> returned
    @PatchMapping("/{id}/return")
    public ResponseEntity<BorrowRecordResponse> returnBook(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                toBorrowRecordResponse(
                        borrowRecordsService.returnBook(id)
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowRecordResponse>> getBorrowRecordsByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByUser(userId)
                        .stream()
                        .map(this::toBorrowRecordResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowRecordResponse>> getBorrowRecordsByBook(
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByBook(bookId)
                        .stream()
                        .map(this::toBorrowRecordResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BorrowRecordResponse>> getBorrowRecordsByStatus(
            @PathVariable BorrowStatus status
    ) {
        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByStatus(status)
                        .stream()
                        .map(this::toBorrowRecordResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<BorrowRecordResponse>>
    getBorrowRecordsByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable BorrowStatus status
    ) {
        return ResponseEntity.ok(
                borrowRecordsService
                        .getBorrowRecordsByUserAndStatus(userId, status)
                        .stream()
                        .map(this::toBorrowRecordResponse)
                        .collect(Collectors.toList())
        );
    }

    // Danh sách quá hạn
    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowRecordResponse>> getOverdueBorrowRecords() {
        return ResponseEntity.ok(
                borrowRecordsService.getOverdueBorrowRecords()
                        .stream()
                        .map(this::toBorrowRecordResponse)
                        .collect(Collectors.toList())
        );
    }

    @PatchMapping("/update-overdue")
    public ResponseEntity<Void> updateOverdueBorrowRecords() {
        borrowRecordsService.updateOverdueBorrowRecords();
        return ResponseEntity.noContent().build();
    }

    private BorrowRecordResponse toBorrowRecordResponse(
            BorrowRecords record
    ) {
        Long userId = null;
        String username = null;
        Long bookId = null;
        String bookTitle = null;

        if (record.getUser() != null) {
            userId = record.getUser().getUserId();
            username = record.getUser().getUsername();
        }

        if (record.getBook() != null) {
            bookId = record.getBook().getBookId();
            bookTitle = record.getBook().getTitle();
        }

        return new BorrowRecordResponse(
                record.getId(),
                userId,
                username,
                bookId,
                bookTitle,
                record.getBorrowDate(),
                record.getDueDate(),
                record.getReturnDate(),
                record.getStatus()
        );
    }
}