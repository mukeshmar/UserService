package live.mukeshtechlab.userservice.controllers;

import live.mukeshtechlab.userservice.dtos.*;
import live.mukeshtechlab.userservice.dtos.ResponseStatus;
import live.mukeshtechlab.userservice.models.Token;
import live.mukeshtechlab.userservice.models.User;
import live.mukeshtechlab.userservice.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) {

        User user = userService.singUp(
                signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword()
        );
        return UserDto.from(user);

    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        try {
            Token token = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            loginResponseDto.setToken(token.getValue());
            loginResponseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {
            loginResponseDto.setStatus(ResponseStatus.FAILURE);
        }

        return loginResponseDto;
    }

    @GetMapping("/validate/{token}")
    private UserDto validateToken(@PathVariable("token") String token) {
        User user = userService.validateToken(token);
        return UserDto.from(user);
    }

    @PatchMapping("/logout")
    public void logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        userService.logout(logoutRequestDto.getToken());
    }

}

