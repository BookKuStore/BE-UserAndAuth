package id.ac.ui.cs.advprog.bookku.bookku_userauth.model;

import java.sql.Date;

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
    uniqueConstraints = { @UniqueConstraint(columnNames = {"username", "email", "phone"}) }
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

    private String bio;

    private String gender;

    private String birthdate;

    private String profileUrl;

}