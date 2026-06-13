package project.librarymanagement.controller;

import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.entity.BorrowRecords.BorrowStatus;
import project.librarymanagement.service.interfaces.IBorrowRecordsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<BorrowRecords>> getAllBorrowRecords() {
        return ResponseEntity.ok(
                borrowRecordsService.getAllBorrowRecords()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRecords> getBorrowRecordById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordById(id)
        );
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecords> borrowBook(
            @RequestParam Long userId,
            @RequestParam Long bookId
    ) {
        return new ResponseEntity<>(
                borrowRecordsService.borrowBook(userId, bookId),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowRecords> returnBook(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                borrowRecordsService.returnBook(id)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowRecords>> getBorrowRecordsByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByUser(userId)
        );
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowRecords>> getBorrowRecordsByBook(
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByBook(bookId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BorrowRecords>> getBorrowRecordsByStatus(
            @PathVariable BorrowStatus status
    ) {
        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByStatus(status)
        );
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<BorrowRecords>> getBorrowRecordsByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable BorrowStatus status
    ) {
        return ResponseEntity.ok(
                borrowRecordsService.getBorrowRecordsByUserAndStatus(
                        userId,
                        status
                )
        );
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowRecords>> getOverdueBorrowRecords() {
        return ResponseEntity.ok(
                borrowRecordsService.getOverdueBorrowRecords()
        );
    }

    @PutMapping("/update-overdue")
    public ResponseEntity<Void> updateOverdueBorrowRecords() {
        borrowRecordsService.updateOverdueBorrowRecords();
        return ResponseEntity.noContent().build();
    }
}

//GET http://localhost:8080/api/borrow-records
//GET http://localhost:8080/api/borrow-records/1
//POST http://localhost:8080/api/borrow-records/borrow?userId=2&bookId=1
//PUT http://localhost:8080/api/borrow-records/1/return
//GET http://localhost:8080/api/borrow-records/user/2
//GET http://localhost:8080/api/borrow-records/book/1
//GET http://localhost:8080/api/borrow-records/status/BORROWED
//GET http://localhost:8080/api/borrow-records/overdue
//PUT http://localhost:8080/api/borrow-records/update-overdue