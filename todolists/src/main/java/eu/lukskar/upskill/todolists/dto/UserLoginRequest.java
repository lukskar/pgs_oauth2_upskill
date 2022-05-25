package eu.lukskar.upskill.todolists.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized @Builder
public class UserLoginRequest {
    private final String username;
    private final String password;
}
