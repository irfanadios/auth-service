package project.irfanadios.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import project.irfanadios.authservice.request.InsertRoleRequest;
import project.irfanadios.authservice.response.InsertRoleResponse;
import project.irfanadios.authservice.service.rolemanagement.RoleService;
import project.irfanadios.authservice.util.response.DataResponseBuilder;

@RestController
@RequestMapping("/v1/role-management")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/role")
    public ResponseEntity<DataResponseBuilder<InsertRoleResponse>> addRole(@Valid @RequestBody InsertRoleRequest request) {
        DataResponseBuilder<InsertRoleResponse> response = roleService.addRole(request);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
