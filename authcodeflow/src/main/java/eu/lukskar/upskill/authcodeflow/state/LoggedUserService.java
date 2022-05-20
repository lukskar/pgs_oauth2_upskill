package eu.lukskar.upskill.authcodeflow.state;

import eu.lukskar.upskill.authcodeflow.dto.SimpleJWTPayload;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoggedUserService {

    private final Map<String, LoggedUserState> loggedUsers;

    public LoggedUserService() {
        this.loggedUsers = new HashMap<>();
    }

    public String logUserIn(SimpleJWTPayload simpleJWTPayload) {
        String userName = simpleJWTPayload.getName();

        LoggedUserState loggedUser = new LoggedUserState();
        loggedUser.setLogged(true);
        loggedUser.setName(userName);
        loggedUser.setNickname(simpleJWTPayload.getNickname());
        loggedUser.setPicture(simpleJWTPayload.getPicture());
        loggedUsers.put(userName, loggedUser);

        return userName;
    }

    public LoggedUserState getUserState(String userName) {
        if (userName == null || userName.isBlank())
            return new LoggedUserState();

        return Optional.of(loggedUsers.get(userName)).orElse(new LoggedUserState());
    }
}
