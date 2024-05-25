package id.ac.ui.cs.advprog.bookku.bookku_userauth.model;

import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "account",
    uniqueConstraints = { @UniqueConstraint(columnNames = {"username"}) }
)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="seq")
    @GenericGenerator(name = "seq", strategy="increment")
    private int id;

    @NonNull
    private String username;

    @JsonIgnore
    @NonNull
    private String password;

    @NonNull
    private String role;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String phone;

    private int cartId;

    private int historyId;

}