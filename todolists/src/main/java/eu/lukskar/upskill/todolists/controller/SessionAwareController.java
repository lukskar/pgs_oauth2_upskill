package eu.lukskar.upskill.todolists.controller;

import eu.lukskar.upskill.todolists.model.DbUserDetails;
import org.springframework.security.core.Authentication;

public interface SessionAwareController {
    default String getUserId(Authentication authentication) {
        return ((DbUserDetails) authentication.getPrincipal()).getId();
    }
}
