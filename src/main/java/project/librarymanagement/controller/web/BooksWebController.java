package project.librarymanagement.controller.web;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.librarymanagement.dto.request.CreateBookRequest;
import project.librarymanagement.dto.request.UpdateBookRequest;
import project.librarymanagement.dto.response.BookResponse;
import project.librarymanagement.entity.Books;
import project.librarymanagement.service.interfaces.IBooksService;
import project.librarymanagement.service.interfaces.ICategoriesService;

@Controller
@RequestMapping("/books")
public class BooksWebController {

    private final IBooksService bookService;
    private final ICategoriesService categoryService;

    public BooksWebController(IBooksService bookService, ICategoriesService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }
    /**
     * 1. GET: Hiển thị danh sách sách (Đã đồng bộ map sang BookResponse)
     */
    @GetMapping
    public String listBooks(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            Model model) {

        try {
            Page<BookResponse> booksPage;

            if (keyword != null && !keyword.trim().isEmpty()) {
                booksPage = bookService.searchBooksByKeyword(keyword.trim(), page, size)
                        .map(this::toBookResponse);
                model.addAttribute("keyword", keyword);
            } else {
                booksPage = bookService.getAllBooks(page, size, sortBy, "asc")
                        .map(this::toBookResponse);
            }

            model.addAttribute("books", booksPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("sortBy", sortBy);

        } catch (Exception e) {
            // Log lỗi và trả về list rỗng thay vì null để tránh lỗi NullPointerException ở view
            model.addAttribute("books", Page.empty());
            model.addAttribute("errorMessage", "Không thể tải danh sách sách.");
        }

        return "books/list";
    }

    /**
     * 2. GET: Hiển thị giao diện Thêm sách mới
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("createBookRequest", new CreateBookRequest());
        model.addAttribute("categories", categoryService.getAllCategories(0, 1000).getContent());
        return "books/create";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PostMapping("/create")
    public String handleCreate(
            @Valid @ModelAttribute("createBookRequest") CreateBookRequest request,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories(0, 1000).getContent());
            return "books/create";
        }

        try {
            bookService.createBook(request);
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Thêm sách thất bại: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories(0, 1000).getContent());
            return "books/create";
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            var book = bookService.getBookById(id);

            UpdateBookRequest updateRequest = new UpdateBookRequest();
            updateRequest.setTitle(book.getTitle());
            updateRequest.setAuthor(book.getAuthor());
            updateRequest.setQuantity(book.getQuantity());
            updateRequest.setIsbn(book.getIsbn());

            if (book.getCategory() != null) {
                updateRequest.setCategoryId(book.getCategory().getCategoryId());
            }

            model.addAttribute("updateBookRequest", updateRequest);
            model.addAttribute("bookId", id);
            model.addAttribute("categories", categoryService.getAllCategories(0, 1000).getContent());
            return "books/edit";
        } catch (Exception e) {
            return "redirect:/books?error=NotFound";
        }
    }

    /**
     * 4. POST: Xử lý submit cập nhật sách (Đã đồng bộ kiểm tra lỗi Validation)
     */
    @PostMapping("/edit/{id}")
    public String handleUpdate(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("updateBookRequest") UpdateBookRequest request,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            System.out.println("==> [LỖI UPDATE] Form chỉnh sửa Sách ID " + id + " gặp lỗi Validation:");

            bindingResult.getFieldErrors().forEach(error ->
                    System.out.println("   -> Trường bị lỗi [" + error.getField() + "]: " + error.getDefaultMessage())
            );

            // câu thông báo tổng quan lên giao diện thay vì im lặng
            model.addAttribute("errorMessage", "Dữ liệu nhập vào chưa hợp lệ! Vui lòng kiểm tra lại các trường.");

            // Nạp lại các dữ liệu cần thiết để giao diện không bị sập trắng trang
            model.addAttribute("bookId", id);
            model.addAttribute("categories", categoryService.getAllCategories(0, 1000).getContent());
            return "books/edit";
        }

        try {
            bookService.updateBook(id, request);

            // Update thành công, chuyển hướng về trang danh sách
            return "redirect:/books";
        } catch (Exception e) {
            System.out.println("==> Lỗi từ tầng Service khi thực hiện cập nhật: " + e.getMessage());

            model.addAttribute("errorMessage", "Cập nhật thất bại: " + e.getMessage());
            model.addAttribute("bookId", id);
            model.addAttribute("categories", categoryService.getAllCategories(0, 1000).getContent());
            return "books/edit";
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, Model model) {
        try {
            bookService.deleteBook(id);

            // Log kiểm tra tiến trình xóa thành công
            System.out.println("==> Xóa thành công sách có ID: " + id);

            return "redirect:/books?success=Deleted";
        } catch (Exception e) {
            System.out.println("==> Lỗi khi xóa sách ID " + id + ": " + e.getMessage());

            // Nếu có lỗi (Ví dụ: Sách này đang được mượn, không thể xóa vì ràng buộc DB)
            return "redirect:/books?error=DeleteFailed";
        }
    }

    private BookResponse toBookResponse(Books book) {
        Long categoryId = null;
        String categoryName = null;

        if (book.getCategory() != null) {
            categoryId = book.getCategory().getCategoryId();
            categoryName = book.getCategory().getCategoryName();
        }

        return new BookResponse(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getQuantity(),
                book.getPublisher(),
                book.getPublishYear(),
                categoryId,
                categoryName
        );
    }
}