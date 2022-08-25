package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.AuthUserDetails;
import eu.lukskar.upskill.todolists.dto.SimpleUserInfo;
import eu.lukskar.upskill.todolists.dto.UserRegistrationRequest;
import eu.lukskar.upskill.todolists.model.DbUserDetails;
import eu.lukskar.upskill.todolists.repository.UserDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDetailsRepository userDetailsRepository;

    public UserService(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    public SimpleUserInfo getUserInfo(final AuthUserDetails userDetails) {
        return SimpleUserInfo.builder().fullName(userDetails.getName()).build();
    }

    public DbUserDetails register(final UserRegistrationRequest userRegistrationRequest, String registrationType) {
        DbUserDetails newUser = DbUserDetails.builder()
                .username(userRegistrationRequest.getUsername())
                .fullName(userRegistrationRequest.getFullName())
                .registrationType(registrationType)
                .email(userRegistrationRequest.getEmail())
                .build();

        return userDetailsRepository.save(newUser);
    }
}
