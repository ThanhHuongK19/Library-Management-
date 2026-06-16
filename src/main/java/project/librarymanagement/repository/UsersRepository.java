package project.librarymanagement.repository;

import project.librarymanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository
        extends JpaRepository<Users, Long> {

    Optional<Users> findUserByUsername(
            String username
    );

    Optional<Users> findUserByEmail(
            String email
    );

    boolean existsUserByUsername(
            String username
    );

    boolean existsUserByEmail(
            String email
    );

    long count();
}