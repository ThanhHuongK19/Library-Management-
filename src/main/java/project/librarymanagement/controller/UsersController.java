package project.librarymanagement.controller;

import project.librarymanagement.dto.request.UpdateUserRequest;
import project.librarymanagement.dto.response.UserResponse;
import project.librarymanagement.entity.Users;
import project.librarymanagement.mapper.UserMapper;
import project.librarymanagement.service.interfaces.IUsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final IUsersService usersService;
    private final UserMapper userMapper;

    public UsersController(IUsersService usersService, UserMapper userMapper) {
        this.usersService = usersService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = usersService.getAllUsers()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                userMapper.toUserResponse(usersService.getUserById(id))
        );
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(
                userMapper.toUserResponse(usersService.getUserByUsername(username))
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(
            @PathVariable String email
    ) {
        return ResponseEntity.ok(
                userMapper.toUserResponse(usersService.getUserByEmail(email))
        );
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody Users user
    ) {
        Users createdUser = usersService.createUser(user);

        return new ResponseEntity<>(
                userMapper.toUserResponse(createdUser),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        Users updatedUser = usersService.updateUser(id, request);

        return ResponseEntity.ok(
                userMapper.toUserResponse(updatedUser)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsUserByUsername(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(
                usersService.existsUserByUsername(username)
        );
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsUserByEmail(
            @PathVariable String email
    ) {
        return ResponseEntity.ok(
                usersService.existsUserByEmail(email)
        );
    }
}