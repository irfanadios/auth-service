package project.irfanadios.authservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.irfanadios.authservice.dto.AccessToken;
import project.irfanadios.authservice.dto.RefreshToken;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    private AccessToken accessToken;
    private RefreshToken refreshToken;
    private List<String> authorities;
}
