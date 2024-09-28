package live.mukeshtechlab.userservice.services;

import live.mukeshtechlab.userservice.models.Token;
import live.mukeshtechlab.userservice.models.User;
import live.mukeshtechlab.userservice.repositories.TokenRepository;
import live.mukeshtechlab.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final TokenRepository tokenRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public User singUp(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Token login(String email, String password) {
        // Check if Email correct or not
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User with email Id: " + email + " not found. Please Sign Up!");
        }
        User user = userOptional.get();

        if (bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            // Password Matches, Now Generate the Token for this User
            Token token = createToken(user);
            return tokenRepository.save(token);
        }
        return null;
    }

    public User validateToken(String token) {
        return null;
    }

    public void logout(String token) {

    }

    private Token createToken(User user) {
        Token token = new Token();
        token.setUser(user);

        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        // Set Expiry Time
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysFromNow = currentDate.plusDays(30);
        Date expiryAt = Date.from(thirtyDaysFromNow.atStartOfDay(ZoneId.systemDefault()).toInstant());

        token.setExpiryAt(expiryAt);

        return token;
    }
}
