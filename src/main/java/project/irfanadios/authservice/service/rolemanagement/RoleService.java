package project.irfanadios.authservice.service.rolemanagement;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import project.irfanadios.authservice.model.Role;
import project.irfanadios.authservice.repository.RoleRepository;
import project.irfanadios.authservice.request.InsertRoleRequest;
import project.irfanadios.authservice.response.InsertRoleResponse;
import project.irfanadios.authservice.util.response.DataResponseBuilder;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public DataResponseBuilder<InsertRoleResponse> addRole(InsertRoleRequest request) {
        Role role = Role.builder().roleName(request.getRoleName()).build();

        role = roleRepository.save(role);

        InsertRoleResponse response = modelMapper.map(role, InsertRoleResponse.class);

        return DataResponseBuilder.<InsertRoleResponse>builder()
            .status(HttpStatus.OK)
            .message("Role Add Success!")
            .data(response)
        .build();
    }
}
