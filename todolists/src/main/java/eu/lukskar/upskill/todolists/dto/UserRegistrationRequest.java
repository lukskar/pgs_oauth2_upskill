package eu.lukskar.upskill.todolists.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized @Builder
public class UserRegistrationRequest {
    private final String username;
    private final String fullName;
    private final String email;
}
