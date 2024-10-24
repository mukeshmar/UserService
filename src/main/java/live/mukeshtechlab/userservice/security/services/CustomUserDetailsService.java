package live.mukeshtechlab.userservice.security.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import live.mukeshtechlab.userservice.models.User;
import live.mukeshtechlab.userservice.repositories.UserRepository;
import live.mukeshtechlab.userservice.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@JsonDeserialize
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with id: " + username + " not found!");
        }

        User user = optionalUser.get();

        // Convert User to UserDetails type
        return new CustomUserDetails(user);

    }
}
