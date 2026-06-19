package project.librarymanagement.controller;

import org.springframework.data.domain.Page;
import project.librarymanagement.dto.response.BorrowRecordResponse;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.mapper.BorrowRecordMapper;
import project.librarymanagement.service.interfaces.IBorrowRecordsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/borrow-records")
public class BorrowRecordsController {

    private final IBorrowRecordsService borrowRecordsService;
    private final BorrowRecordMapper recordMapper;

    public BorrowRecordsController(IBorrowRecordsService borrowRecordsService, BorrowRecordMapper recordMapper) {
        this.borrowRecordsService = borrowRecordsService;
        this.recordMapper = recordMapper;
    }

    @GetMapping
    public ResponseEntity<Page<BorrowRecordResponse>> getAllBorrowRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Gọi service đã phân trang
        Page<BorrowRecordResponse> responsePage = borrowRecordsService.getAllBorrowRecords(page, size)
                .map(recordMapper::toBorrowRecordResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BorrowRecordResponse>> getBorrowRecordsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Gọi service tìm theo user với phân trang
        Page<BorrowRecordResponse> responsePage = borrowRecordsService.getBorrowRecordsByUser(userId, page, size)
                .map(recordMapper::toBorrowRecordResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRecordResponse> getBorrowRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(recordMapper.toBorrowRecordResponse(borrowRecordsService.getBorrowRecordById(id)));
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
}