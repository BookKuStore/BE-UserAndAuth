package id.ac.ui.cs.advprog.bookku.bookku_userauth.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refreshToken")
public class RefreshToken {

    @Id
    @GeneratedValue
    private Integer tokenId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "Id", columnDefinition = "integer")
    private Account account;

    @NonNull
    private String token;
    
}