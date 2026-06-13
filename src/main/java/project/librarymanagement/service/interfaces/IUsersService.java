package project.librarymanagement.service.interfaces;

import project.librarymanagement.entity.Users;
import java.util.List;

public interface IUsersService {
    List<Users> getAllUsers();

    Users getUserById(long id);

    Users createUser(Users user);

    Users updateUser(long id, Users user);

    void deleteUser(long id);

    Users findByUsername(String username);
}
