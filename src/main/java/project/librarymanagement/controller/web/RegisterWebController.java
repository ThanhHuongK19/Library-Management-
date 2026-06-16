package project.librarymanagement.controller.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.librarymanagement.dto.request.RegisterRequest;
import project.librarymanagement.service.interfaces.IAuthService;

@Controller
public class RegisterWebController {

    private final IAuthService authService;

    public RegisterWebController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * 1. GET: Hiển thị giao diện Đăng ký tài khoản
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        // Nạp object RegisterRequest trống để liên kết dữ liệu với form Thymeleaf
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register"; // Tìm đến file src/main/resources/templates/auth/register.html
    }

    /**
     * 2. POST: Xử lý submit dữ liệu khi người dùng ấn nút "Đăng ký"
     */
    @PostMapping("/register")
    public String handleRegister(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult bindingResult,
            Model model) {

        // Nếu dữ liệu nhập vào vi phạm Validation (như trống tên, trùng format email...)
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            // Gọi tầng Service để xử lý tạo tài khoản mới giống AuthController cũ
            authService.register(request);

            // Đăng ký thành công -> Điều hướng người dùng về trang đăng nhập
            return "redirect:/login";

        } catch (Exception e) {
            // Bắt các lỗi nghiệp vụ từ Service (Ví dụ: Tên tài khoản hoặc Email đã tồn tại trong DB)
            model.addAttribute("errorMessage", "Đăng ký thất bại: " + e.getMessage());
            return "auth/register";
        }
    }
}