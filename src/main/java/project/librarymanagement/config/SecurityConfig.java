package project.librarymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.librarymanagement.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";
    private static final String LIBRARIAN = "LIBRARIAN";
    private static final String USER = "USER";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // Cấu hình Session linh hoạt: Tạo session khi phía Web UI Thymeleaf cần dùng
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth
                        // 1. Giao diện Web UI Công khai (Cho phép truy cập trực tiếp từ trình duyệt công khai)
                        .requestMatchers(
                                "/", "/home", "/login", "/register",
                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/books/**" // 🔥 ĐÃ ĐỒNG BỘ: Cho phép chạy toàn bộ luồng giao diện Sách (list, create, edit) công khai
                        ).permitAll()

                        // 2. Các Endpoint REST API Công khai & Tài liệu Swagger
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger", "/swagger/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 3. Phân quyền cho REST API đọc dữ liệu (GET /api/**)
                        .requestMatchers(HttpMethod.GET, "/api/books/**", "/api/categories/**")
                        .hasAnyRole(USER, ADMIN, LIBRARIAN)

                        // 4. Phân quyền cho REST API Quản lý dữ liệu (POST, PUT, PATCH, DELETE /api/**)
                        .requestMatchers(HttpMethod.POST, "/api/books/**", "/api/categories/**").hasAnyRole(ADMIN, LIBRARIAN)
                        .requestMatchers(HttpMethod.PUT, "/api/books/**", "/api/categories/**").hasAnyRole(ADMIN, LIBRARIAN)
                        .requestMatchers(HttpMethod.PATCH, "/api/books/**", "/api/categories/**").hasAnyRole(ADMIN, LIBRARIAN) // Thêm bọc PATCH cho API
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**", "/api/categories/**").hasRole(ADMIN)

                        // 5. Các REST API Quản trị nâng cao
                        .requestMatchers("/api/users/**", "/api/roles/**").hasRole(ADMIN)
                        .requestMatchers("/api/borrow-records/**").hasAnyRole(ADMIN, LIBRARIAN)

                        // 6. Giao diện Web UI cần bảo mật (Khi nào bạn làm đến tính năng này thì bật phân quyền sau)
                        .requestMatchers("/categories/**", "/borrow-records/**").authenticated()

                        // Tất cả các request phát sinh còn lại đều cần xác thực
                        .anyRequest().authenticated()
                )
                // Cấu hình Form Login hệ thống dành riêng cho giao diện Web UI
                .formLogin(form -> form
                        .loginPage("/login")               // GET /login dẫn đến form đăng nhập UI
                        .loginProcessingUrl("/perform_login") // URL ngầm để Spring Security hứng POST data từ form login
                        .defaultSuccessUrl("/home", true)  // Đăng nhập thành công chuyển hướng thẳng về trang chủ UI
                        .permitAll()
                )
                // Cấu hình Đăng xuất dành cho giao diện Web UI
                .logout(logout -> logout
                        .logoutUrl("/logout") // Khớp với th:action="@{/logout}"
                        .logoutSuccessUrl("/home") // Đăng xuất xong chuyển hướng về trang chủ
                        .invalidateHttpSession(true) // Xóa session trên server
                        .clearAuthentication(true)   // Xóa quyền trong SecurityContext
                        .permitAll()
                )
                // Đính kèm bộ lọc kiểm tra JWT Token trước cho các đường dẫn REST API (/api/**)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}