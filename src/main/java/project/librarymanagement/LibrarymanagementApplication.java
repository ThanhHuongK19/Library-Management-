package project.librarymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
public class LibrarymanagementApplication {

	public static void main(String[] args) {
		killProcessOnPort(9000);
		SpringApplication.run(LibrarymanagementApplication.class, args);
	}
	private static void killProcessOnPort(int port) {
		try {
			String os = System.getProperty("os.name").toLowerCase();

			// Chỉ áp dụng tự động trên Windows (Hệ điều hành bạn đang dùng)
			if (os.contains("win")) {
				// Lệnh tìm PID đang chiếm port
				Process process = Runtime.getRuntime().exec("cmd.exe /c netstat -ano | findstr :" + port);
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;

				while ((line = reader.readLine()) != null) {
					if (line.contains("LISTENING")) {
						// Cắt chuỗi để lấy mã PID ở cuối dòng
						String[] parts = line.trim().split("\\s+");
						String pid = parts[parts.length - 1];

						// Thực hiện ép hủy tiến trình ngầm cũ
						Runtime.getRuntime().exec("taskkill /F /PID " + pid);
						System.out.println("==> [Bảo mật Hệ thống] Đã tự động giải phóng ứng dụng chạy ngầm chiếm port " + port + " (PID: " + pid + ")");
					}
				}
				reader.close();
			}
		} catch (Exception e) {
			System.out.println("Không thể tự động giải phóng port: " + e.getMessage());
		}
	}
}
