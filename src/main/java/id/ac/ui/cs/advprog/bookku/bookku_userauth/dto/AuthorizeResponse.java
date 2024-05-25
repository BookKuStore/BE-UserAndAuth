package id.ac.ui.cs.advprog.bookku.bookku_userauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@AllArgsConstructor()
@NoArgsConstructor()
public class AuthorizeResponse {

    private boolean authorized;
    private String message;

}
