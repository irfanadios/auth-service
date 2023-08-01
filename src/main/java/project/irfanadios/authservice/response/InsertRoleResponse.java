package project.irfanadios.authservice.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsertRoleResponse {
    private UUID roleId;
    private String roleName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
