package eu.lukskar.upskill.authcodeflow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleJWTPayload {
    private String nickname;
    private String name;
    private String picture;
}
