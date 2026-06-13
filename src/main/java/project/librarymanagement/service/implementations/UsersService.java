package project.librarymanagement.service.implementations;

import project.librarymanagement.entity.Users;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import project.librarymanagement.repository.UsersRepository;
import project.librarymanagement.service.interfaces.IUsersService;

import java.util.List;

@Service
public class UsersService implements IUsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users getUserById(long id) {
        return usersRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );
    }

    @Override
    public Users createUser(Users user) {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BadRequestException("Username cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BadRequestException("Email cannot be empty");
        }

        if (usersRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        return usersRepository.save(user);
    }

    @Override
    public Users updateUser(long id, Users user) {

        Users existingUser = getUserById(id);

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BadRequestException("Username cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BadRequestException("Email cannot be empty");
        }

        if (!existingUser.getUsername().equalsIgnoreCase(user.getUsername())
                && usersRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (!existingUser.getEmail().equalsIgnoreCase(user.getEmail())
                && usersRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setFullName(user.getFullName());
        existingUser.setActive(user.isActive());
        existingUser.setRoles(user.getRoles());

        return usersRepository.save(existingUser);
    }

    @Override
    public void deleteUser(long id) {
        Users user = getUserById(id);

        // xóa mềm
        user.setActive(false);
        usersRepository.save(user);
    }

    @Override
    public Users findByUsername(String username) {

        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be empty");
        }

        return usersRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with username: " + username
                        )
                );
    }
}