package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.UserLoginRequest;
import eu.lukskar.upskill.todolists.dto.UserRegistrationRequest;
import eu.lukskar.upskill.todolists.model.DbUserDetails;
import eu.lukskar.upskill.todolists.repository.UserDetailsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDetailsRepository userDetailsRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(final UserLoginRequest userLoginRequest) {
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
        if (authenticationResult.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticationResult);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public void logout(final HttpServletRequest httpRequest) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.setClearAuthentication(true);
        logoutHandler.setInvalidateHttpSession(true);
        logoutHandler.logout(httpRequest, null, null);
    }

    public void register(final UserRegistrationRequest userRegistrationRequest) {
        String passwordHash = passwordEncoder.encode(userRegistrationRequest.getPassword());
        DbUserDetails newUser = DbUserDetails.builder()
                .username(userRegistrationRequest.getUsername())
                .passwordHash(passwordHash)
                .fullName(userRegistrationRequest.getFullName())
                .build();
        userDetailsRepository.save(newUser);
    }
}
