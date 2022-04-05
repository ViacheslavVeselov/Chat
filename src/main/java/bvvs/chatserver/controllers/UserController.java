package bvvs.chatserver.controllers;

import bvvs.chatserver.exception.ResourceNotFoundException;
import bvvs.chatserver.models.User;
import bvvs.chatserver.models.dto.AuthRequestDto;
import bvvs.chatserver.models.dto.UserDto;
import bvvs.chatserver.repo.UserRepository;
import bvvs.chatserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/users/", produces = "application/hal+json")
@AllArgsConstructor
public class UserController {
    final UserRepository userRepository;
    final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public User create(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("{userId}")
    @ResponseStatus(OK)
    public User getById(@PathVariable String userId) {
        return userRepository.findById(UUID.fromString(userId)).orElseThrow(() ->
                new ResourceNotFoundException(Map.of("No user with id",  userId)));
    }

    @PostMapping("login")
    public String login(@RequestBody AuthRequestDto authRequestDto) {
        return userService.login(authRequestDto);
    }
}
