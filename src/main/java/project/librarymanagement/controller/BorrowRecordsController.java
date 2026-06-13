package project.librarymanagement.controller;

import project.librarymanagement.entity.BorrowRecords;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.librarymanagement.service.interfaces.IBorrowRecordsService;

import java.util.List;

@RestController
@RequestMapping("/api/borrow-records")
public class BorrowRecordsController {

    private final IBorrowRecordsService borrowRecordsService;

    public BorrowRecordsController(
            IBorrowRecordsService borrowRecordsService) {
        this.borrowRecordsService = borrowRecordsService;
    }

    @GetMapping("/get-all-borrow-records")
    public ResponseEntity<List<BorrowRecords>> getAllBorrowRecords() {
        return ResponseEntity.ok(
                borrowRecordsService.getAllBorrowRecords()
        );
    }

    @GetMapping("/get-borrow-record/{id}")
    public ResponseEntity<BorrowRecords> getBorrowRecordById(
            @PathVariable long id) {

        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordById(id)
        );
    }

    @PostMapping("/borrow-book")
    public ResponseEntity<BorrowRecords> borrowBook(
            @RequestParam long userId,
            @RequestParam long bookId) {

        return ResponseEntity.ok(
                borrowRecordsService.borrowBook(userId, bookId)
        );
    }

    @PutMapping("/return/{borrowRecordId}")
    public ResponseEntity<BorrowRecords> returnBook(
            @PathVariable long borrowRecordId) {

        return ResponseEntity.ok(
                borrowRecordsService.returnBook(borrowRecordId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowRecords>> getBorrowRecordsByUser(
            @PathVariable long userId) {

        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByUser(userId)
        );
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowRecords>> getBorrowRecordsByBook(
            @PathVariable long bookId) {

        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByBook(bookId)
        );
    }
}