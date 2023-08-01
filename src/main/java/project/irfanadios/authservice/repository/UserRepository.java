package project.irfanadios.authservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import project.irfanadios.authservice.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserIdAndIsActiveTrue(UUID userId);
    Optional<User> findByEmailAndIsActiveTrue(String email);
}
