package live.mukeshtechlab.userservice;

import live.mukeshtechlab.userservice.controllers.UserController;
import live.mukeshtechlab.userservice.dtos.SignUpRequestDto;
import live.mukeshtechlab.userservice.dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserControllerTests {

    @Autowired
    private UserController userController;

    @Test
    public void testSignUp(){

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setName("Mukesh Mar");
        signUpRequestDto.setEmail("mukesh@abc.com");
        signUpRequestDto.setPassword("123456");

        UserDto userDto = userController.signUp(signUpRequestDto);

        System.out.println("Name: " + userDto.getName());
        System.out.println("Email: " + userDto.getEmail());
    }
}
