package eu.lukskar.upskill.jwttimeapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "UserDetails")
public class UserDetails {
    @Id private String id;
    private String userName;
    private String passwordHash;
}
