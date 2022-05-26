package eu.lukskar.upskill.todolists.state;

import eu.lukskar.upskill.todolists.model.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class AppState {

    private final Map<String, UserDetails> loggedUsers = new HashMap<>();

    public void logUserIn(final UserDetails loggedUser, final String JSESSIONID) {
        loggedUsers.put(JSESSIONID, loggedUser);
    }

    public Optional<UserDetails> getLoggedUser(String JSESSIONID) {
        if (loggedUsers.containsKey(JSESSIONID))
            return Optional.of(loggedUsers.get(JSESSIONID));
        else
            return Optional.empty();
    }

    public Optional<String> getLoggedUserId(String JSESSIONID) {
        Optional<UserDetails> userDetails = getLoggedUser(JSESSIONID);
        return userDetails.map(UserDetails::getId);
    }

    public String getLoggedUserIdOrThrow(String JSESSIONID) {
        return getLoggedUser(JSESSIONID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED))
                .getId();
    }

    public boolean logUserOff(String JSESSIONID) {
        UserDetails removedUser = loggedUsers.remove(JSESSIONID);
        return Objects.nonNull(removedUser);
    }
}
