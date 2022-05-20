package eu.lukskar.upskill.authcodeflow;

import eu.lukskar.upskill.authcodeflow.state.LoggedUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class AuthController {

    private final AuthService authService;
    private final LoggedUserService loggedUserService;

    public AuthController(AuthService authService, LoggedUserService loggedUserService) {
        this.authService = authService;
        this.loggedUserService = loggedUserService;
    }

    @GetMapping("/auth/login")
    public String login() {
        return "redirect:" + authService.buildLoginUrl();
    }

    @GetMapping("/auth/callback")
    public String callback(
            @RequestParam String code,
            HttpServletResponse response
    ) throws IOException, InterruptedException {
        String loggedUserName = authService.handleAuthorizationCallback(code);

        Cookie loggedUserCookie = new Cookie("logged-user-name", loggedUserName);
        loggedUserCookie.setPath("/");
        response.addCookie(loggedUserCookie);

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(
            Model model,
            @CookieValue(value = "logged-user-name", defaultValue = "") String userName
    ) {
        model.addAttribute("loggedUser", loggedUserService.getUserState(userName));
        return "home";
    }
}
