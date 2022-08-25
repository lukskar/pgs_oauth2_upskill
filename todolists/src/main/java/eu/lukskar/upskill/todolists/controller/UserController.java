package eu.lukskar.upskill.todolists.controller;

import eu.lukskar.upskill.todolists.dto.AuthUserDetails;
import eu.lukskar.upskill.todolists.dto.SimpleUserInfo;
import eu.lukskar.upskill.todolists.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
