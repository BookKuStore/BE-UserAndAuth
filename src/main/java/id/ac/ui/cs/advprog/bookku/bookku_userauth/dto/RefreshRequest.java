package id.ac.ui.cs.advprog.bookku.bookku_userauth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@AllArgsConstructor()
@NoArgsConstructor()
public class RefreshRequest {

    @NotBlank(message = "Refresh token cannot be empty")
    private String refreshToken;

}
