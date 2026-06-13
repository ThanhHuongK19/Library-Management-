package project.librarymanagement.service.implementations;

import project.librarymanagement.entity.Roles;
import project.librarymanagement.entity.Roles.RoleName;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.exception.ResourceNotFoundException;
import project.librarymanagement.repository.RolesRepository;
import project.librarymanagement.service.interfaces.IRolesService;
import org.springframework.stereotype.Service;

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
    public Roles getRoleById(Long roleId) {
        return rolesRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with id: " + roleId
                        )
                );
    }

    @Override
    public Roles getRoleByName(RoleName roleName) {
        return rolesRepository.findRoleByRoleName(roleName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + roleName
                        )
                );
    }

    @Override
    public Roles createRole(Roles role) {
        if (rolesRepository.existsRoleByRoleName(role.getRoleName())) {
            throw new BadRequestException(
                    "Role already exists: " + role.getRoleName()
            );
        }

        return rolesRepository.save(role);
    }

    @Override
    public Roles updateRole(Long roleId, Roles role) {
        Roles existingRole = getRoleById(roleId);

        if (!existingRole.getRoleName().equals(role.getRoleName())
                && rolesRepository.existsRoleByRoleName(role.getRoleName())) {
            throw new BadRequestException(
                    "Role already exists: " + role.getRoleName()
            );
        }

        existingRole.setRoleName(role.getRoleName());

        return rolesRepository.save(existingRole);
    }

    @Override
    public void deleteRole(Long roleId) {
        Roles role = getRoleById(roleId);
        rolesRepository.delete(role);
    }
}