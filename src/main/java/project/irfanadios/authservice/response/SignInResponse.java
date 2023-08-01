package project.irfanadios.authservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    private String accessToken;
    private String subject;
    private List<String> authorities;
    private LocalDateTime issuedTime;
    private LocalDateTime expiredTime;
}
