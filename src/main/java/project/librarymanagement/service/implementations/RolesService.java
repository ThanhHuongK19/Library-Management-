package project.librarymanagement.service.implementations;

import project.librarymanagement.entity.Roles;
import project.librarymanagement.entity.Roles.RoleName;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import project.librarymanagement.repository.RolesRepository;
import project.librarymanagement.service.interfaces.IRolesService;

import java.util.List;

@Service
public class RolesService implements IRolesService {

    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }

    @Override
    public Roles getRoleById(long id) {
        return rolesRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with id: " + id
                        )
                );
    }

    @Override
    public Roles createRole(Roles role) {

        if (rolesRepository.existsByRoleName(role.getRoleName())) {
            throw new BadRequestException(
                    "Role already exists: " + role.getRoleName()
            );
        }

        return rolesRepository.save(role);
    }

    @Override
    public Roles updateRole(long id, Roles role) {

        Roles existingRole = getRoleById(id);

        if (!existingRole.getRoleName().equals(role.getRoleName())
                && rolesRepository.existsByRoleName(role.getRoleName())) {

            throw new BadRequestException(
                    "Role already exists: " + role.getRoleName()
            );
        }

        existingRole.setRoleName(role.getRoleName());

        return rolesRepository.save(existingRole);
    }

    @Override
    public void deleteRole(long id) {

        Roles role = getRoleById(id);

        if (role.getUsers() != null && !role.getUsers().isEmpty()) {
            throw new BadRequestException(
                    "Cannot delete role because it is assigned to users"
            );
        }

        rolesRepository.delete(role);
    }

    @Override
    public Roles findByRoleName(RoleName roleName) {

        return rolesRepository.findByRoleName(roleName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + roleName
                        )
                );
    }
}