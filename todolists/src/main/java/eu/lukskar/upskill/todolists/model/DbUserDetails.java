package eu.lukskar.upskill.todolists.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("UserDetails")
public class DbUserDetails {
    @Id private String id;
    private String email;
    private String username;
    private String passwordHash;
    private String fullName;
    private String registrationType;
}
