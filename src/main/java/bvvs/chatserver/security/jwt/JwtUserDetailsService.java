package bvvs.chatserver.security.jwt;

import bvvs.chatserver.exception.ResourceNotFoundException;
import bvvs.chatserver.models.User;
import bvvs.chatserver.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(email);
        JwtUserDetails jwtUserDetails = JwtUserDetails.from(byEmail
                .orElseThrow(() -> new ResourceNotFoundException(Map.of("User with such email does not exist", email))));
        log.info("Loaded user with email -> {}", email);
        return jwtUserDetails;
    }
}
