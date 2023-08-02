package project.irfanadios.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    private String token;
    private String subject;
    private LocalDateTime issuedTime;
    private LocalDateTime expiredTime;
}
