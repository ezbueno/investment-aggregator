package dev.ezandro.investmentaggregator.controller;

import dev.ezandro.investmentaggregator.dto.UpdateUserDTO;
import dev.ezandro.investmentaggregator.entity.User;
import dev.ezandro.investmentaggregator.dto.CreateUserDTO;
import dev.ezandro.investmentaggregator.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDTO createUserDTO) {
        var userId = this.userService.createUser(createUserDTO);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "userId") String userId) {
        var user = this.userService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(this.userService.listUsers());
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<Void> updateUserById(@PathVariable(value = "userId") String userId,
                                               @RequestBody UpdateUserDTO updateUserDTO) {
        this.userService.updateUserById(userId, updateUserDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable(value = "userId") String userId) {
        this.userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}