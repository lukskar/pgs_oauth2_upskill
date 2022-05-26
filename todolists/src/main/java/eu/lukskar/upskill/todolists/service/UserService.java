package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.UserLoginRequest;
import eu.lukskar.upskill.todolists.dto.UserRegistrationRequest;
import eu.lukskar.upskill.todolists.model.UserDetails;
import eu.lukskar.upskill.todolists.repository.UserDetailsRepository;
import eu.lukskar.upskill.todolists.state.AppState;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final AppState appState;
    private final UserDetailsRepository userDetailsRepository;

    public UserService(AppState appState, UserDetailsRepository userDetailsRepository) {
        this.appState = appState;
        this.userDetailsRepository = userDetailsRepository;
    }

    public void login(final UserLoginRequest userLoginRequest, final String JSESSIONID) {
        Example<UserDetails> example = Example.of(UserDetails.builder().username(userLoginRequest.getUsername()).build());
        UserDetails userDetails = userDetailsRepository.findOne(example).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        String passwordProvided = userLoginRequest.getPassword();
        String passwordHash = userDetails.getPasswordHash();

        if (!new BCryptPasswordEncoder().matches(passwordProvided, passwordHash))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        appState.logUserIn(userDetails, JSESSIONID);
    }

    public void logout(final String JSESSIONID) {
        appState.logUserOff(JSESSIONID);
    }

    public void register(final UserRegistrationRequest userRegistrationRequest) {
        String passwordHash = new BCryptPasswordEncoder().encode(userRegistrationRequest.getPassword());
        UserDetails newUser = UserDetails.builder()
                .username(userRegistrationRequest.getUsername())
                .passwordHash(passwordHash)
                .fullName(userRegistrationRequest.getFullName())
                .build();
        userDetailsRepository.save(newUser);
    }
}
