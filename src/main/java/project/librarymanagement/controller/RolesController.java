package project.librarymanagement.controller;

import project.librarymanagement.entity.Roles;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.librarymanagement.service.interfaces.IRolesService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolesController {

    private final IRolesService rolesService;

    public RolesController(IRolesService rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping("/get-all-roles")
    public ResponseEntity<List<Roles>> getAllRoles() {
        return ResponseEntity.ok(rolesService.getAllRoles());
    }

    @GetMapping("/get-role/{id}")
    public ResponseEntity<Roles> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(rolesService.getRoleById(id));
    }

    @PatchMapping("/update-role/{id}")
    public ResponseEntity<Roles> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody Roles role) {

        return ResponseEntity.ok(rolesService.updateRole(id, role));
    }

    @DeleteMapping("/delete-role/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        rolesService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}