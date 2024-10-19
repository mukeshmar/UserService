package live.mukeshtechlab.userservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.mukeshtechlab.userservice.dtos.SendEmailDto;
import live.mukeshtechlab.userservice.models.Token;
import live.mukeshtechlab.userservice.models.User;
import live.mukeshtechlab.userservice.repositories.TokenRepository;
import live.mukeshtechlab.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private TokenRepository tokenRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            TokenRepository tokenRepository,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public User singUp(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        //First encrypt the password using BCrypt Algorithm before storing into the DB.
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));

        // Push sendEmail event to Kafka for sending welcome emails to users

        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setTo(email);
        sendEmailDto.setSubject("Welcome to the Family!");
        sendEmailDto.setBody("Your account has been successfully created, and you can now take advantage of all the features we offer");

        try {
            System.out.println("Pushing the event inside Kafka.");
            kafkaTemplate.send("sendEmail", objectMapper.writeValueAsString(sendEmailDto));
        }
        catch (JsonProcessingException exception){
            throw new RuntimeException(exception);
        }

        // Now Return
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
        // Check if the token is valid
        Optional<Token> tokenOptional = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(
                token,
                false,
                new Date()
        );

        if (tokenOptional.isEmpty()) {
            return null;
        }

        return tokenOptional.get().getUser();
    }

    public void logout(String token) {
        // Check if the token is valid or not
        Optional<Token> tokenOptional = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(
                token,
                false,
                new Date()
        );

        if (tokenOptional.isEmpty()) {
            // Invalid token
            return;
        }
        Token tokenFromDB = tokenOptional.get();
        tokenFromDB.setDeleted(true);
        tokenRepository.save(tokenFromDB);
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
