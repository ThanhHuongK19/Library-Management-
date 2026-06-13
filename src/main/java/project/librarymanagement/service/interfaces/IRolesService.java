package project.librarymanagement.service.interfaces;

import project.librarymanagement.entity.Roles;
import project.librarymanagement.entity.Roles.RoleName;

import java.util.List;

public interface IRolesService {

    List<Roles> getAllRoles();

    Roles getRoleById(Long roleId);

    Roles getRoleByName(RoleName roleName);

    Roles createRole(Roles role);

    Roles updateRole(Long roleId, Roles role);

    void deleteRole(Long roleId);
}