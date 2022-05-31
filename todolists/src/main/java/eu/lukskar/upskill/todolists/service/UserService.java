package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.UserLoginRequest;
import eu.lukskar.upskill.todolists.dto.UserRegistrationRequest;
import eu.lukskar.upskill.todolists.model.DbUserDetails;
import eu.lukskar.upskill.todolists.repository.UserDetailsRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    public UserService(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsRepository.findByUsername(username);
    }

    public void login(final UserLoginRequest userLoginRequest) {
        throw new NotImplementedException();
    }

    public void logout() {
        throw new NotImplementedException();
    }

    public void register(final UserRegistrationRequest userRegistrationRequest) {
        String passwordHash = new BCryptPasswordEncoder().encode(userRegistrationRequest.getPassword());
        DbUserDetails newUser = DbUserDetails.builder()
                .username(userRegistrationRequest.getUsername())
                .passwordHash(passwordHash)
                .fullName(userRegistrationRequest.getFullName())
                .build();
        userDetailsRepository.save(newUser);
    }
}
