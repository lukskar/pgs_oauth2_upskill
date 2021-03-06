package eu.lukskar.upskill.todolists.controller;

import eu.lukskar.upskill.todolists.dto.AuthUserDetails;
import eu.lukskar.upskill.todolists.dto.SimpleUserInfo;
import eu.lukskar.upskill.todolists.dto.UserLoginRequest;
import eu.lukskar.upskill.todolists.dto.UserRegistrationRequest;
import eu.lukskar.upskill.todolists.model.RegistrationType;
import eu.lukskar.upskill.todolists.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/info")
    public SimpleUserInfo userInfo(@AuthenticationPrincipal final AuthUserDetails userDetails) {
        return userService.getUserInfo(userDetails);
    }

    @PostMapping("/user/login")
    public void login(@RequestBody final UserLoginRequest userLoginRequest) {
        userService.login(userLoginRequest);
    }

    @PostMapping("/user/logout")
    public void logout(final HttpServletRequest httpRequest) {
        userService.logout(httpRequest);
    }

    @PostMapping("/user/register")
    public void register(@RequestBody final UserRegistrationRequest userRegistrationRequest) {
        userService.register(userRegistrationRequest, RegistrationType.REGULAR);
    }
}
