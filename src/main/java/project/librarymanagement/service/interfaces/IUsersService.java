package project.librarymanagement.service.interfaces;

import project.librarymanagement.dto.request.UpdateUserRequest;
import project.librarymanagement.entity.Users;

import java.util.List;

public interface IUsersService {

    List<Users> getAllUsers();

    Users getUserById(Long userId);

    Users getUserByUsername(String username);

    Users getUserByEmail(String email);

    Users createUser(Users user);

    Users updateUser(
            Long userId,
            UpdateUserRequest request
    );

    void deleteUser(Long userId);

    boolean existsUserByUsername(
            String username
    );

    boolean existsUserByEmail(
            String email
    );
}