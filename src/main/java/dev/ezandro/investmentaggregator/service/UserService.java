package dev.ezandro.investmentaggregator.service;

import dev.ezandro.investmentaggregator.dto.CreateUserDTO;
import dev.ezandro.investmentaggregator.dto.UpdateUserDTO;
import dev.ezandro.investmentaggregator.entity.User;
import dev.ezandro.investmentaggregator.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(CreateUserDTO createUserDTO) {
        var entity = new User(
                UUID.randomUUID(),
                createUserDTO.username(),
                createUserDTO.email(),
                createUserDTO.password(),
                Instant.now(),
                null);
        var userSaved = this.userRepository.save(entity);
        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        return this.userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers() {
        return this.userRepository.findAll();
    }

    public void updateUserById(String userId, UpdateUserDTO updateUserDTO) {
        var id = UUID.fromString(userId);
        var user = this.userRepository.findById(id);

        if (user.isPresent()) {
            var updateUser = user.get();

            if (Objects.nonNull(updateUserDTO.username())) {
                updateUser.setUsername(updateUserDTO.username());
            }

            if (Objects.nonNull(updateUserDTO.password())) {
                updateUser.setPassword(updateUserDTO.password());
            }

            this.userRepository.save(updateUser);
        }
    }

    public void deleteUserById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = this.userRepository.existsById(id);

        if (userExists) {
            this.userRepository.deleteById(id);
        }
    }
}