package project.irfanadios.authservice.service.usermanagement;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.irfanadios.authservice.dto.AccessToken;
import project.irfanadios.authservice.dto.RefreshToken;
import project.irfanadios.authservice.model.User;
import project.irfanadios.authservice.model.UserRole;
import project.irfanadios.authservice.model.UserRoleId;
import project.irfanadios.authservice.repository.RoleRepository;
import project.irfanadios.authservice.repository.UserRepository;
import project.irfanadios.authservice.repository.UserRoleRepository;
import project.irfanadios.authservice.request.RefreshTokenRequest;
import project.irfanadios.authservice.request.SignInRequest;
import project.irfanadios.authservice.request.SignUpRequest;
import project.irfanadios.authservice.response.SignInResponse;
import project.irfanadios.authservice.util.implementation.UserDetailsImpl;
import project.irfanadios.authservice.util.jwt.JwtUtils;
import project.irfanadios.authservice.util.response.DataResponseBuilder;

import java.time.ZoneId;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Value("${irfanadios.app.jwtExpirationMs}")
    private Long jwtExpirationMs;

    @Value("${irfanadios.app.jwtRefreshExpirationMs}")
    private Long jwtRefreshExpirationMs;

    public DataResponseBuilder<SignInResponse> signIn(SignInRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Date now = new Date();
        Date accessExpired = new Date(now.getTime() + jwtExpirationMs);
        Date refreshExpired = new Date(now.getTime() + jwtRefreshExpirationMs);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String subject = userDetails.getUsername();

        AccessToken accessToken = AccessToken.builder()
                .token(jwtUtils.generateJwtToken(authentication, now, accessExpired))
                .subject(subject)
                .issuedTime(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .expiredTime(accessExpired.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(jwtUtils.generateJwtToken(authentication, now, refreshExpired))
                .subject(subject)
                .issuedTime(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .expiredTime(refreshExpired.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();

        List<String> authorities = userRoleRepository.findByUserId(userDetails.getUserId()).stream().map(
                userRole -> userRole.getRole().getRoleName()
        ).toList();

        SignInResponse response = SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authorities(authorities)
                .build();

        return DataResponseBuilder.<SignInResponse>builder()
                .status(HttpStatus.OK)
                .message("Sign In Success!")
                .data(response)
                .build();
    }

    @Transactional
    public DataResponseBuilder<Object> signUp(SignUpRequest request) {
        if (userRepository.findByEmailAndIsActiveTrue(request.getEmail()).isPresent()) {
            return DataResponseBuilder.builder()
                    .message("Email already exist. Use another email.")
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        final User savedUser = userRepository.save(newUser);

        List<UserRole> userRoles = new LinkedList<>();

        List<UUID> uuidAuthorities =
                request.getAuthorities().stream()
                        .map(UUID::fromString)
                        .toList();

        roleRepository.findByRoleIdIn(uuidAuthorities).forEach(role -> {
            UserRoleId userRoleId = UserRoleId.builder()
                    .userId(savedUser.getUserId())
                    .roleId(role.getRoleId())
                    .build();
            UserRole userRole = UserRole.builder()
                    .userRoleId(userRoleId)
                    .user(savedUser)
                    .role(role)
                    .build();
            userRoles.add(userRole);
        });

        userRoleRepository.saveAll(userRoles);

        return DataResponseBuilder.builder()
                .status(HttpStatus.OK)
                .message("Register User Success!")
                .build();
    }

    public DataResponseBuilder<SignInResponse> refreshToken(RefreshTokenRequest request) {
        if (!jwtUtils.validateJwtToken(request.getToken())) {
            return DataResponseBuilder.<SignInResponse>builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("Your Refresh Token is Expired Too. Please Re-LogIn.")
                    .build();
        }

        String subject = jwtUtils.getUserNameFromJwtToken(request.getToken());
        Optional<User> user = userRepository.findByEmailAndIsActiveTrue(subject);
        if (user.isEmpty()) {
            return DataResponseBuilder.<SignInResponse>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("You entering token that subject is not found.")
                    .build();
        }

        Date now = new Date();
        Date accessExpired = new Date(now.getTime() + jwtExpirationMs);
        Date refreshExpired = new Date(now.getTime() + jwtRefreshExpirationMs);

        AccessToken accessToken = AccessToken.builder()
                .subject(subject)
                .token(jwtUtils.generateJwtToken(subject, now, accessExpired))
                .issuedTime(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .expiredTime(accessExpired.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        RefreshToken refreshToken = RefreshToken.builder()
                .subject(subject)
                .token(jwtUtils.generateJwtToken(subject, now, refreshExpired))
                .issuedTime(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .expiredTime(refreshExpired.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();

        List<String> authorities = userRoleRepository.findByUserId(user.get().getUserId()).stream().map(
                userRole -> userRole.getRole().getRoleName()
        ).toList();

        SignInResponse data = SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authorities(authorities)
                .build();

        return DataResponseBuilder.<SignInResponse>builder()
                .status(HttpStatus.OK)
                .message("Access and Refresh Token is Regenerated.")
                .data(data)
                .build();
    }
}
