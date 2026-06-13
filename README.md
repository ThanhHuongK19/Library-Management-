# Library-Management-

PHÁT TRIỂN HỆ THỐNG QUẢN LÝ THƯ VIỆN SỐ 

1. TỔNG QUAN DỰ ÁN 
Dự án yêu cầu xây dựng một ứng dụng quản lý thư viện hiện đại nhằm tối ưu hóa việc lưu trữ và tra cứu sách. Hệ thống cần đảm bảo khả năng vận hành ổn định trên nền tảng Spring Boot, tích hợp giao diện người dùng trực quan và các dịch vụ API chuẩn hóa cho việc mở rộng trong tương lai. 

2. YÊU CẦU CÔNG NGHỆ 

Ngôn ngữ lập trình: Java phiên bản 17 trở lên. 

Framework chính: Spring Boot 

Quản trị dữ liệu: Spring Data JPA kết hợp cơ sở dữ liệu MySQL hoặc PostgreSQL. 

Giao diện người dùng: Thymeleaf kết hợp thư viện Bootstrap. 

Bảo mật: Spring Security và thuật toán mã hóa BCrypt. 

Công cụ hỗ trợ: Maven, Postman và Docker. 

3. LỘ TRÌNH THỰC HIỆN VÀ NỘI DUNG CHI TIẾT 

1: Thiết lập cấu trúc và phát triển giao diện người dùng 

Khởi tạo dự án theo cấu trúc tiêu chuẩn bao gồm các lớp Controller, Service, Repository và Entity. 

Vận dụng cơ chế Dependency Injection và IoC để quản lý các thành phần trong hệ thống. 

Xây dựng trang chủ hiển thị danh mục sách và chi tiết từng sản phẩm bằng Thymeleaf. 

Triển khai điều hướng dữ liệu thông qua Model và ModelAndView từ phía máy chủ. 

2: Quản trị cơ sở dữ liệu và truy vấn nâng cao 

Thiết kế sơ đồ thực thể bao gồm Sách, Danh mục và Người dùng với các mối quan hệ tương ứng. 

Triển khai các thao tác thêm, sửa, xóa dữ liệu thông qua JpaRepository. 

Xây dựng chức năng tìm kiếm sách dựa trên phương thức Query Method và ngôn ngữ JPQL. 

Áp dụng kỹ thuật phân trang và sắp xếp dữ liệu để tối ưu hóa hiệu suất hiển thị danh sách sách. 

3: Xây dựng REST API và thiết lập cơ chế bảo mật 

Thiết kế hệ thống RESTful API hỗ trợ các phương thức GET, POST, PUT và DELETE trả về dữ liệu định dạng JSON. 

Thực hiện kiểm chứng dữ liệu đầu vào bằng các ràng buộc Validation như NotNull, Size hoặc Min. 

Xây dựng bộ xử lý ngoại lệ tập trung để chuẩn hóa thông báo lỗi cho người dùng. 

Triển khai Spring Security để phân quyền truy cập: chỉ quản trị viên mới có quyền thay đổi dữ liệu trong khi người dùng phổ thông chỉ được phép xem thông tin. 

4: Tối ưu hóa cấu hình và đóng gói triển khai 

Phân tách môi trường phát triển và môi trường vận hành thực tế bằng Spring Profiles. 

Sử dụng Maven để đóng gói ứng dụng thành tệp tin định dạng JAR hoàn chỉnh. 

Xây dựng Dockerfile để container hóa ứng dụng và sẵn sàng cho việc triển khai trên các nền tảng đám mây. (optional) 

4. TIÊU CHÍ ĐÁNH GIÁ TỔNG THỂ 

Tính đúng đắn: hệ thống chạy ổn định và không phát sinh lỗi nghiệp vụ nghiêm trọng. 

Tính bảo mật: các endpoint quan trọng được bảo vệ chặt chẽ và mật khẩu được mã hóa an toàn. 

Chất lượng mã nguồn: mã nguồn trình bày sạch sẽ, tuân thủ quy ước đặt tên và cấu trúc phân lớp rõ ràng. 

Khả năng ứng dụng: giao diện dễ sử dụng và API phản hồi đúng chuẩn trạng thái HTTP. 

Đảm bảo áp dụng đầy đủ kiến thức trong file syllabus về spring boot. 

Áp dụng được AI coding, AI first trong bài là điểm cộng. 

Có thể tùy biến ý tưởng trên theo sở thích nhưng đảm bảo áp dụng đủ kiến thức yêu cầu, các nhiều tính năng các được đánh giá cao. 

 
