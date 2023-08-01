package project.irfanadios.authservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import project.irfanadios.authservice.model.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleIdAndIsActiveTrue(UUID roleId);
    List<Role> findByRoleIdIn(List<UUID> roleIds);
}
