package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.AuthUserDetails;
import eu.lukskar.upskill.todolists.dto.UserRegistrationRequest;
import eu.lukskar.upskill.todolists.model.DbUserDetails;
import eu.lukskar.upskill.todolists.model.RegistrationType;
import eu.lukskar.upskill.todolists.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OAuth2AuthenticationManager extends DefaultOAuth2UserService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserService userService;

    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User authenticatedUser =  super.loadUser(oAuth2UserRequest);
        Optional<DbUserDetails> searchDetails = userDetailsRepository.findByEmail(authenticatedUser.getAttribute("email"));
        DbUserDetails userDetails = null;

        if (searchDetails.isEmpty()) {
            userDetails = userService.register(
                    UserRegistrationRequest.builder()
                            .username(authenticatedUser.getAttribute("name"))
                            .fullName(authenticatedUser.getAttribute("name"))
                            .email(authenticatedUser.getAttribute("email"))
                            .build(), RegistrationType.OAUTH2_GOOGLE);
        } else {
            userDetails = searchDetails.get();
        }

        return AuthUserDetails.builder()
                .id(userDetails.getId())
                .name(userDetails.getFullName())
                .username(userDetails.getUsername())
                .password(userDetails.getPasswordHash())
                .registrationType(RegistrationType.OAUTH2_GOOGLE)
                .attributes(authenticatedUser.getAttributes())
                .build();
    }
}
