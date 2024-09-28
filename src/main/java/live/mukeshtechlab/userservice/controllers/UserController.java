package live.mukeshtechlab.userservice.controllers;

import live.mukeshtechlab.userservice.dtos.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        return null;
    }

    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        return null;
    }

    private UserDto validateToken(String token){
        return null;
    }

    public LogoutResponseDto logout(@RequestBody LogoutRequestDto logoutRequestDto){
        return null;
    }

}

