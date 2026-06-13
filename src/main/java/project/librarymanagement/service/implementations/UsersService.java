package project.librarymanagement.service.implementations;

import project.librarymanagement.entity.Users;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import project.librarymanagement.repository.UsersRepository;
import project.librarymanagement.service.interfaces.IUsersService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService implements IUsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(
            UsersRepository usersRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users getUserById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: "
                                        + userId
                        )
                );
    }

    @Override
    public Users getUserByUsername(
            String username
    ) {
        return usersRepository
                .findUserByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: "
                                        + username
                        )
                );
    }

    @Override
    public Users getUserByEmail(
            String email
    ) {
        return usersRepository
                .findUserByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: "
                                        + email
                        )
                );
    }

    @Override
    public Users createUser(
            Users user
    ) {
        if (usersRepository
                .existsUserByUsername(
                        user.getUsername()
                )) {

            throw new BadRequestException(
                    "Username already exists"
            );
        }

        if (usersRepository
                .existsUserByEmail(
                        user.getEmail()
                )) {

            throw new BadRequestException(
                    "Email already exists"
            );
        }

        user.setPasswordHash(
                passwordEncoder.encode(
                        user.getPasswordHash()
                )
        );

        return usersRepository.save(user);
    }

    @Override
    public Users updateUser(
            Long userId,
            Users user
    ) {
        Users existingUser =
                getUserById(userId);

        if (!existingUser.getUsername()
                .equals(user.getUsername())
                &&
                usersRepository.existsUserByUsername(
                        user.getUsername()
                )) {

            throw new BadRequestException(
                    "Username already exists"
            );
        }

        if (!existingUser.getEmail()
                .equals(user.getEmail())
                &&
                usersRepository.existsUserByEmail(
                        user.getEmail()
                )) {

            throw new BadRequestException(
                    "Email already exists"
            );
        }

        existingUser.setUsername(
                user.getUsername()
        );

        existingUser.setEmail(
                user.getEmail()
        );

        existingUser.setFullName(
                user.getFullName()
        );

        existingUser.setIsActive(
                user.getIsActive()
        );

        return usersRepository.save(
                existingUser
        );
    }

    @Override
    public void deleteUser(
            Long userId
    ) {
        Users user =
                getUserById(userId);

        usersRepository.delete(user);
    }

    @Override
    public boolean existsUserByUsername(
            String username
    ) {
        return usersRepository
                .existsUserByUsername(
                        username
                );
    }

    @Override
    public boolean existsUserByEmail(
            String email
    ) {
        return usersRepository
                .existsUserByEmail(
                        email
                );
    }
}