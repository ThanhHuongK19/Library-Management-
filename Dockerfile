# Dùng Java17 để run
FROM eclipse-temurin:17-jdk-alpine

# Tạo folder làm việc trong container
WORKDIR /app

# Copy file JAR đã build vào container
COPY target/*.jar app.jar

# sử dụng profile triển khai tốt hơn trên cloud
#ENV SPRING_PROFILES_ACTIVE=prod

# Khai báo port run
EXPOSE 9000

# Lệnh run khi container khởi động
ENTRYPOINT ["java", "-jar", "app.jar"]

# Build Docker image
# docker build -t library-management-app

# Run container
# docker run -p 9000:9000 library-management-app

# Truyền profile khi run
# docker run -p 8080:8080 \-e SPRING_PROFILES_ACTIVE=prod \library-management-app