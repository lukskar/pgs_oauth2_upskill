package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.UserLoginRequest;
import eu.lukskar.upskill.todolists.dto.UserRegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    public void login(final UserLoginRequest userLoginRequest) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not yet implemented");
    }

    public void register(final UserRegistrationRequest userRegistrationRequest) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not yet implemented");
    }
}
