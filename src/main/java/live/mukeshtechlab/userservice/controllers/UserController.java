package live.mukeshtechlab.userservice.controllers;

import live.mukeshtechlab.userservice.dtos.*;
import live.mukeshtechlab.userservice.models.Token;
import live.mukeshtechlab.userservice.models.User;
import live.mukeshtechlab.userservice.services.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) {

        User user = userService.singUp(
                signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword()
        );
        return UserDto.from(user);

    }

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

    private UserDto validateToken(String token) {
        return null;
    }

    public LogoutResponseDto logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        return null;
    }

}

