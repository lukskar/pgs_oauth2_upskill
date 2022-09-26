package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.AuthUserDetails;
import eu.lukskar.upskill.todolists.dto.UserRegistrationRequest;
import eu.lukskar.upskill.todolists.model.DbUserDetails;
import eu.lukskar.upskill.todolists.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OidcUsersManagerService extends OidcUserService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    public OidcUser loadUser(OidcUserRequest oidcUserRequest) {
        OidcUser oidcUser =  super.loadUser(oidcUserRequest);
        Optional<DbUserDetails> searchDetails = userDetailsRepository.findByEmail(oidcUser.getAttribute("email"));
        DbUserDetails userDetails = null;

        if (searchDetails.isEmpty()) {
            userDetails = userService.register(
                    UserRegistrationRequest.builder()
                            .username(oidcUser.getAttribute("nickname"))
                            .fullName(oidcUser.getAttribute("name"))
                            .email(oidcUser.getAttribute("email"))
                            .build(), registrationType(oidcUser));
            subscriptionService.createTrialSubscription(userDetails.getId());
        } else {
            userDetails = searchDetails.get();
        }

        return AuthUserDetails.builder()
                .id(userDetails.getId())
                .name(userDetails.getFullName())
                .username(userDetails.getUsername())
                .registrationType(userDetails.getRegistrationType())
                .attributes(oidcUser.getAttributes())
                .claims(oidcUser.getClaims())
                .userInfo(oidcUser.getUserInfo())
                .idToken(oidcUser.getIdToken())
                .build();
    }

    private String registrationType(OidcUser oidcUser) {
        return oidcUser.getClaims().get("sub").toString().split("\\|")[0];
    }
}
