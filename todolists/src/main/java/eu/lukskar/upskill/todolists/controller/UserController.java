package eu.lukskar.upskill.todolists.controller;

import eu.lukskar.upskill.todolists.dto.UserLoginRequest;
import eu.lukskar.upskill.todolists.dto.UserRegistrationRequest;
import eu.lukskar.upskill.todolists.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/login")
    public void login(@RequestBody final UserLoginRequest userLoginRequest) {
        userService.login(userLoginRequest);
    }

    @PostMapping("/user/register")
    public void register(@RequestBody final UserRegistrationRequest userRegistrationRequest) {
        userService.register(userRegistrationRequest);
    }
}
