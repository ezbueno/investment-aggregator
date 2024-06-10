package dev.ezandro.investmentaggregator.service;

import dev.ezandro.investmentaggregator.dto.CreateUserDTO;
import dev.ezandro.investmentaggregator.dto.UpdateUserDTO;
import dev.ezandro.investmentaggregator.entity.User;
import dev.ezandro.investmentaggregator.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(value = MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Nested
    class createUser {
        @Test
        @DisplayName(value = "Should create a user successfully")
        void shouldCreateUserSuccessfully() {
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null);

            Mockito.doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            var input = new CreateUserDTO("username", "email@email.com", "123");
            var output = userService.createUser(input);

            Assertions.assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();

            Assertions.assertEquals(input.username(), userCaptured.getUsername());
            Assertions.assertEquals(input.email(), userCaptured.getEmail());
            Assertions.assertEquals(input.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName(value = "Should throw exception when an error occurs during user creation")
        void shouldThrowExceptionWhenCreatingUserFails() {
            Mockito.doThrow(new RuntimeException()).when(userRepository).save(Mockito.any());

            var input = new CreateUserDTO("username", "email@email.com", "123");

            Assertions.assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }
    }

    @Nested
    class getUserById {
        @Test
        @DisplayName(value = "Should return user when getting user by ID and user is found")
        void shouldReturnUserWhenGetUserByIdAndUserIsFound() {
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null);

            Mockito.doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            var output = userService.getUserById(user.getUserId().toString());

            Assertions.assertTrue(output.isPresent());
            Assertions.assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName(value = "Should return empty when getting user by ID and user is not found")
        void shouldReturnEmptyWhenGetUserByIdAndUserNotFound() {
            var userId = UUID.randomUUID();

            Mockito.doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            var output = userService.getUserById(userId.toString());

            Assertions.assertTrue(output.isEmpty());
            Assertions.assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class listUsers {
        @Test
        @DisplayName(value = "Should return all users successfully")
        void shouldReturnAllUsersSuccessfully() {
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null);

            var userList = List.of(user);

            Mockito.doReturn(userList).when(userRepository).findAll();

            var output = userService.listUsers();

            Assertions.assertNotNull(output);
            Assertions.assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class updateUserById {
        @Test
        @DisplayName(value = "Should update user when user is found by ID")
        void shouldUpdateUserWhenUserIsFoundById() {
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null);

            var updateUserDTO = new UpdateUserDTO("newUsername", "newPassword");

            Mockito.doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            Mockito.doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            userService.updateUserById(user.getUserId().toString(), updateUserDTO);

            Assertions.assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            Assertions.assertEquals(updateUserDTO.username(), userCaptured.getUsername());
            Assertions.assertEquals(updateUserDTO.password(), userCaptured.getPassword());

            Mockito.verify(userRepository, Mockito.times(1)).findById(uuidArgumentCaptor.getValue());
            Mockito.verify(userRepository, Mockito.times(1)).save(user);
        }

        @Test
        @DisplayName(value = "Should not update user when user does not exist")
        void shouldNotUpdateUserWhenUserDoesNotExist() {
            var updateUserDTO = new UpdateUserDTO("newUsername", "newPassword");

            var userId = UUID.randomUUID();

            Mockito.doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            userService.updateUserById(userId.toString(), updateUserDTO);

            Assertions.assertEquals(userId, uuidArgumentCaptor.getValue());

            Mockito.verify(userRepository, Mockito.times(1)).findById(uuidArgumentCaptor.getValue());
            Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any());
        }
    }

    @Nested
    class deleteById {
        @Test
        @DisplayName(value = "Should delete user successfully when user exists")
        void shouldDeleteUserWhenUserExists() {
            Mockito.doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());
            Mockito.doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            userService.deleteUserById(userId.toString());

            var idList = uuidArgumentCaptor.getAllValues();

            Assertions.assertEquals(userId, idList.get(0));
            Assertions.assertEquals(userId, idList.get(1));

            Mockito.verify(userRepository, Mockito.times(1)).existsById(idList.get(0));
            Mockito.verify(userRepository, Mockito.times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName(value = "Should not delete user when user does not exist")
        void shouldNotDeleteUserWhenUserNotExists() {
            Mockito.doReturn(false).when(userRepository).existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            userService.deleteUserById(userId.toString());

            Assertions.assertEquals(userId, uuidArgumentCaptor.getValue());

            Mockito.verify(userRepository, Mockito.times(1)).existsById(uuidArgumentCaptor.getValue());
            Mockito.verify(userRepository, Mockito.times(0)).deleteById(Mockito.any());
        }
    }
}