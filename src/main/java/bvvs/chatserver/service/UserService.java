package bvvs.chatserver.service;

import bvvs.chatserver.exception.ResourceNotFoundException;
import bvvs.chatserver.models.User;
import bvvs.chatserver.models.dto.AuthRequestDto;
import bvvs.chatserver.models.dto.EditUserDto;
import bvvs.chatserver.models.dto.UserDto;
import bvvs.chatserver.repo.RoleRepository;
import bvvs.chatserver.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public User createUser(UserDto userDto) {
        String roleId = userDto.getRoleId();
        return userRepository.save(User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password((userDto.getPassword()))
                .photoPathToFile(userDto.getPhotoPathToFile())
                .role(roleRepository.findById(UUID.fromString(roleId)).orElseThrow(() ->
                        new ResourceNotFoundException(Map.of("Role with this id does not exist", roleId))))
                .build());
    }

    public User tryGetUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Map.of("Such user does not exist", userId.toString())));
    }

    public User tryGetUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(Map.of("User with such email does not exist", email)));
    }

    public String login(AuthRequestDto requestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(),
                requestDto.getPassword()));

        User user = tryGetUserByEmail(requestDto.getEmail());
        return tokenProvider.createToken(user.getEmail(), user.getRole());
    }

    public User editUser(User user, EditUserDto editUserDto) {
        String name = editUserDto.getName();
        String email = editUserDto.getEmail();
        String password = editUserDto.getPassword();
        String photoPathToFile = editUserDto.getPhotoPathToFile();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhotoPathToFile(photoPathToFile);
        return userRepository.save(user);
    }

    public User banUser(User user, boolean banned) {
        user.setBanned(banned);
        return userRepository.save(user);
    }
}
