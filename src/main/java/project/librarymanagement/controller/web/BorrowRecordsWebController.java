package project.librarymanagement.controller.web;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.librarymanagement.dto.response.BorrowRecordResponse;
import project.librarymanagement.entity.Books;
import project.librarymanagement.entity.BorrowRecords;
import project.librarymanagement.entity.BorrowRecords.BorrowStatus;
import project.librarymanagement.entity.Users;
import project.librarymanagement.repository.UsersRepository;
import project.librarymanagement.service.interfaces.IBooksService;
import project.librarymanagement.service.interfaces.IBorrowRecordsService;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/borrow-records")
public class BorrowRecordsWebController {

    private final IBorrowRecordsService borrowRecordsService;
    private final IBooksService bookService;
    private final UsersRepository usersRepository;

    public BorrowRecordsWebController(IBorrowRecordsService borrowRecordsService,
                                      IBooksService bookService,
                                      UsersRepository usersRepository) {
        this.borrowRecordsService = borrowRecordsService;
        this.bookService = bookService;
        this.usersRepository = usersRepository;
    }

    /**
     * Lấy ID người dùng đang đăng nhập hệ thống một cách an toàn
     */
    private Long getCurrentUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object principal = auth.getPrincipal();

        try {
            Method getUserIdMethod = principal.getClass().getMethod("getUserId");
            return (Long) getUserIdMethod.invoke(principal);
        } catch (Exception e1) {
            try {
                Method getIdMethod = principal.getClass().getMethod("getId");
                return (Long) getIdMethod.invoke(principal);
            } catch (Exception e2) {
                String username = auth.getName();
                return usersRepository.findUserByUsername(username)
                        .map(Users::getUserId)
                        .orElse(null);
            }
        }
    }

    /**
     * GET: "/" - Hiển thị danh sách mượn trả
     * - USER: Chỉ thấy lịch sử cá nhân của mình
     * - ADMIN/LIBRARIAN: Thấy toàn bộ hệ thống
     */
    @GetMapping
    public String listBorrowRecords(
            @RequestParam(value = "status", required = false) BorrowStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isStaff = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_LIBRARIAN"));

        Specification<BorrowRecords> spec = Specification.allOf();

        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) -> cb.between(root.get("borrowDate"),
                    Timestamp.valueOf(startDate.atStartOfDay()), Timestamp.valueOf(endDate.plusDays(1).atStartOfDay())));
        }

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        if (!isStaff) {
            Long userId = getCurrentUserId(auth);
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("userId"), userId));
        }

        Page<BorrowRecordResponse> responsePage = borrowRecordsService.getRecords(spec, page, size).map(this::toBorrowRecordResponse);

        model.addAttribute("records", responsePage.getContent());
        model.addAttribute("recordPage", responsePage);
        model.addAttribute("statuses", BorrowStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("isUserView", !isStaff);

        return "borrows/list";
    }

    /**
     * GET: "/borrow" - Hiển thị form đăng ký mượn
     */
    @GetMapping("/borrow")
    public String showBorrowForm(@RequestParam(value = "bookId", required = false) Long bookId, Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long currentUserId = getCurrentUserId(auth);
            String currentUsername = (auth != null) ? auth.getName() : "Độc giả";

            model.addAttribute("currentUserId", currentUserId != null ? currentUserId : 0L);
            model.addAttribute("currentUsername", currentUsername);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            model.addAttribute("borrowDateStr", LocalDate.now().format(formatter));
            model.addAttribute("dueDateStr", LocalDate.now().plusDays(14).format(formatter));

            if (bookId != null) {
                Books selectedBook = bookService.getBookById(bookId);
                model.addAttribute("selectedBook", selectedBook);
                model.addAttribute("bookId", bookId);
            }

            var availableBooks = bookService.getAllBooks(0, 100, "title", "asc").getContent();
            model.addAttribute("booksList", availableBooks);

            // Gửi thêm danh sách tất cả USER cho Admin/Librarian chọn từ dropdown nếu muốn thay vì nhập tay
            List<Users> allUsers = usersRepository.findAll();
            model.addAttribute("usersList", allUsers);

        } catch (Exception e) {
            System.out.println("==> Lỗi hiển thị Form: " + e.getMessage());
        }
        return "borrows/create";
    }

    /**
     * POST: "/borrow" - Xử lý lưu thông tin mượn sách
     */
    @PostMapping("/borrow")
    public String handleBorrowBook(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam("bookId") Long bookId,
            Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdminOrLibrarian = auth != null && auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_LIBRARIAN"));

            if (!isAdminOrLibrarian) {
                // Nếu là USER: Bắt buộc lấy ID từ Session đăng nhập của chính họ để lưu
                userId = getCurrentUserId(auth);
                if (userId == null) {
                    throw new IllegalStateException("Không tìm thấy thông tin phiên đăng nhập độc giả!");
                }
            } else {
                // Nếu là ADMIN/LIBRARIAN: GIỮ NGUYÊN userId từ form gửi lên (ID của người được mượn hộ)
                if (userId == null || userId == 0) {
                    throw new IllegalArgumentException("Vui lòng nhập hoặc chọn một Độc giả hợp lệ!");
                }
            }

            // Gọi service thực hiện lưu trữ xuống DB
            borrowRecordsService.borrowBook(userId, bookId);
            return "redirect:/borrow-records?success=Borrowed";

        } catch (Exception e) {
            var availableBooks = bookService.getAllBooks(0, 100, "title", "asc").getContent();
            model.addAttribute("booksList", availableBooks);
            model.addAttribute("errorMessage", "Thao tác thất bại: " + e.getMessage());
            model.addAttribute("userId", userId);
            model.addAttribute("bookId", bookId);
            return "borrows/create";
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PostMapping("/{id}/return")
    public String handleReturnBook(@PathVariable("id") Long id) {
        try {
            borrowRecordsService.returnBook(id);
            return "redirect:/borrow-records?success=Returned";
        } catch (Exception e) {
            return "redirect:/borrow-records?error=true";
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PostMapping("/update-overdue")
    public String handleUpdateOverdue() {
        try {
            borrowRecordsService.updateOverdueBorrowRecords();
            return "redirect:/borrow-records?success=OverdueUpdated";
        } catch (Exception e) {
            return "redirect:/borrow-records?error=true";
        }
    }

    private BorrowRecordResponse toBorrowRecordResponse(BorrowRecords record) {
        Long uId = null; String uName = null; Long bId = null; String bTitle = null;
        if (record.getUser() != null) {
            uId = record.getUser().getUserId();
            uName = record.getUser().getUsername();
        }
        if (record.getBook() != null) {
            bId = record.getBook().getBookId();
            bTitle = record.getBook().getTitle();
        }
        return new BorrowRecordResponse(
                record.getId(), uId, uName, bId, bTitle,
                record.getBorrowDate(), record.getDueDate(), record.getReturnDate(), record.getStatus()
        );
    }
}