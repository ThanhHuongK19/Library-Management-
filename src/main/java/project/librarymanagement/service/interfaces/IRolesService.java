package project.librarymanagement.service.interfaces;

import project.librarymanagement.entity.Roles;
import java.util.List;
import project.librarymanagement.entity.Roles.RoleName;

public interface IRolesService {
    List<Roles> getAllRoles();

    Roles getRoleById(long id);

    Roles createRole(Roles role);

    Roles updateRole(long id, Roles role);

    void deleteRole(long id);

    Roles findByRoleName(RoleName roleName);
}
