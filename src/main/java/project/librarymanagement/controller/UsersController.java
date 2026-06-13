package project.librarymanagement.controller;

import project.librarymanagement.entity.Users;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.librarymanagement.service.interfaces.IUsersService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final IUsersService usersService;

    public UsersController(IUsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable long id) {
        return ResponseEntity.ok(usersService.getUserById(id));
    }

    @PostMapping("/create-user")
    public ResponseEntity<Users> createUser(
            @Valid @RequestBody Users user) {

        return new ResponseEntity<>(
                usersService.createUser(user),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<Users> updateUser(
            @PathVariable long id,
            @Valid @RequestBody Users user) {

        return ResponseEntity.ok(usersService.updateUser(id, user));
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-username/{username}")
    public ResponseEntity<Users> findByUsername(
            @PathVariable String username) {

        return ResponseEntity.ok(usersService.findByUsername(username));
    }
}
