package project.librarymanagement.repository;

import project.librarymanagement.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository
        extends JpaRepository<Roles, Long> {

    Optional<Roles> findRoleByRoleName(
            Roles.RoleName roleName
    );

    boolean existsRoleByRoleName(
            Roles.RoleName roleName
    );
}