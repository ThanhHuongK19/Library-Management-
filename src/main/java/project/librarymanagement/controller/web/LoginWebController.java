package project.librarymanagement.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.librarymanagement.dto.request.LoginRequest;
import project.librarymanagement.dto.response.AuthResponse;
import project.librarymanagement.service.CustomUserDetailsService; // Thêm import này
import project.librarymanagement.service.interfaces.IAuthService;

@Controller
public class LoginWebController {

    private final IAuthService authService;
    private final CustomUserDetailsService userDetailsService; // 🔥 Thêm Service này để lấy thông tin User quyền hạn

    public LoginWebController(IAuthService authService, CustomUserDetailsService userDetailsService) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    /**
     * 🔥 ĐÃ ĐỒNG BỘ: Cập nhật cơ chế nạp phiên đăng nhập cho Spring Security
     */
    @PostMapping("/login")
    public String handleLogin(
            @Valid @ModelAttribute("loginRequest") LoginRequest request,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest httpServletRequest, // Thay đổi từ HttpSession sang HttpServletRequest để lấy ngữ cảnh tạo Session
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        try {
            // 1. Gọi tầng nghiệp vụ để kiểm tra tài khoản, mật khẩu
            AuthResponse authResponse = authService.login(request);

            // 2. Lấy thông tin User kèm các vai trò (Roles/Authorities) từ Database
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // 3. Tạo đối tượng chứng thực chuẩn của Spring Security
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            // 4. 🔥 QUAN TRỌNG NHẤT: Lưu đối tượng chứng thực vào Context của hệ thống bảo mật
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 5. Lưu thêm dữ liệu cần thiết riêng vào Session (để lấy Token hiển thị hoặc dùng cho việc khác nếu cần)
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); // Ép ghi nhận Session Security
            session.setAttribute("token", authResponse.getToken());
            session.setAttribute("username", request.getUsername());

            return "redirect:/home";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Tài khoản hoặc mật khẩu không chính xác!");
            return "auth/login";
        }
    }
}