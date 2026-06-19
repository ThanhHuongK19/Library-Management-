package project.librarymanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.librarymanagement.dto.response.UserResponse;
import project.librarymanagement.entity.Users;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring") // Đăng ký làm Spring Bean để Controller có thể nhúng vào
public interface UserMapper {

    @Mapping(
            target = "roles",
            expression = "java(user.getRoles().stream().map(role -> role.getRoleName().name()).collect(java.util.stream.Collectors.toSet()))"
    )
    UserResponse toUserResponse(Users user);
}