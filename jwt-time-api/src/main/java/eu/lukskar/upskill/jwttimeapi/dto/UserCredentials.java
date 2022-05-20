package eu.lukskar.upskill.jwttimeapi.dto;

import lombok.Value;

@Value
public class UserCredentials {
    String username;
    String password;
}
