package project.irfanadios.authservice.util.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResponseBuilder<T> {
    private HttpStatus status;
    private String message;
    private List<T> data;
}
