package eu.lukskar.upskill.todolists.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("UserDetails")
public class UserDetails {
    @Id private String id;
    private String username;
    private String passwordHash;
    private String fullName;
}
