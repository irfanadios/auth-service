package project.irfanadios.authservice.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsertRoleRequest {
    @NotBlank(message = "Role Name is required.")
    private String roleName;
}
