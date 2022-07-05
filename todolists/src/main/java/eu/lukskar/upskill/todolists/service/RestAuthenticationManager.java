package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.AuthUserDetails;
import eu.lukskar.upskill.todolists.model.DbUserDetails;
import eu.lukskar.upskill.todolists.repository.UserDetailsRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestAuthenticationManager implements AuthenticationManager {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public RestAuthenticationManager(UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<DbUserDetails> searchDetails = userDetailsRepository.findByUsername(authentication.getPrincipal().toString());
        if (searchDetails.isEmpty()) {
            return authentication;
        }

        DbUserDetails dbUserDetails = searchDetails.get();
        boolean passwordMatches = passwordEncoder.matches(authentication.getCredentials().toString(), dbUserDetails.getPasswordHash());

        if (passwordMatches) {
            AuthUserDetails authenticatedUser = AuthUserDetails.builder()
                    .id(dbUserDetails.getId())
                    .name(dbUserDetails.getFullName())
                    .username(dbUserDetails.getUsername())
                    .password(dbUserDetails.getPasswordHash())
                    .registrationType(dbUserDetails.getRegistrationType())
                    .build();
            return new UsernamePasswordAuthenticationToken(authenticatedUser, authenticatedUser.getPassword(), authenticatedUser.getAuthorities());
        } else {
            return authentication;
        }
    }
}
