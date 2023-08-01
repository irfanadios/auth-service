package project.irfanadios.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.irfanadios.authservice.model.UserRole;
import project.irfanadios.authservice.model.UserRoleId;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    @Query(nativeQuery = true, value = "SELECT * FROM user_role ur where ur.user_id = :userId ORDER BY ur.updated_time")
    List<UserRole> findByUserId(@Param("userId") UUID userId);
}
