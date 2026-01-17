package github.maxsuel.agregadordeinvestimentos.service;

import github.maxsuel.agregadordeinvestimentos.dto.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.dto.UpdateUserDto;
import github.maxsuel.agregadordeinvestimentos.entity.User;
import github.maxsuel.agregadordeinvestimentos.exceptions.UserNotFoundException;
import github.maxsuel.agregadordeinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    @DisplayName("Tests for Creating User")
    public class CreateUser {

        @Test
        @DisplayName("Should create user with success")
        public void shouldCreateUserWithSuccess() {
            // Arrange
            var input = new CreateUserDto("username", "email@email.com", "123");
            var userSaved = new User("username", "email@email.com", "123");
            userSaved.setUserId(UUID.randomUUID());
            
            doReturn(userSaved)
                    .when(userRepository)
                    .save(any(User.class));

            // Act
            var output = userService.createUser(input);

            // Assert
            assertThat(output).isNotNull();
            assertThat(output).isEqualTo(userSaved.getUserId());
            verify(userRepository, times(1)).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Tests for Updating User")
    public class UpdateUser {

        @Test
        @DisplayName("Should update user when it exists")
        public void shouldUpdateUserWhenUserExists() {
            // Arrange
            var userId = UUID.randomUUID();
            var updateDto = new UpdateUserDto("newUsername", "newPassword");
            var existingUser = new User("oldUsername", "old@email.com", "oldPassword");
            existingUser.setUserId(userId);

            doReturn(Optional.of(existingUser))
                    .when(userRepository)
                    .findById(userId);

            doReturn(existingUser)
                    .when(userRepository)
                    .save(any(User.class));

            // Act
            userService.updateUserById(userId.toString(), updateDto);

            // Assert
            verify(userRepository).save(userArgumentCaptor.capture());
            var userCaptured = userArgumentCaptor.getValue();
            
            assertThat(userCaptured.getUsername()).isEqualTo(updateDto.username());
            assertThat(userCaptured.getPassword()).isEqualTo(updateDto.password());
        }

        @Test
        @DisplayName("Should throw exception when user does not exist in update")
        public void shouldThrowExceptionWhenUserDoesNotExist() {
            // Arrange
            var userId = UUID.randomUUID().toString();
            var updateDto = new UpdateUserDto("username", "password");
            doReturn(Optional.empty()).when(userRepository).findById(any(UUID.class));

            // Act & Assert
            assertThatThrownBy(() -> userService.updateUserById(userId, updateDto))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("User not found with ID: " + userId);
        }
    }

    @Nested
    @DisplayName("Tests for Getting User by ID")
    public class getUserById {

        @Test
        @DisplayName("Should get user by id with success when optional is present")
        // Arrange
        public void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {
            var user = new User(
                UUID.randomUUID(),
                "username",
                "email@email.com",
                "password",
                Instant.now(),
                null
            );

            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());
            
            // Act
            var output = userService.getUserById(user.getUserId().toString());

            // Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should get user by id with success when optional is empty")
        // Arrange
        public void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {
            var userId = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());
            
            // Act
            var output = userService.getUserById(userId.toString());

            // Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    @DisplayName("Tests for Listing Users")
    public class listUsers {

        @Test
        @DisplayName("Should return all users with success")
        public void shouldReturnAllUsersWithSuccess() {
            // Arrange
            var user = new User(
                UUID.randomUUID(),
                "username",
                "email@email.com",
                "password",
                Instant.now(),
                null
            );

            var userList = List.of(user);

            doReturn(userList)
                    .when(userRepository)
                    .findAll();

            // Act
            var output = userService.listAllUsers();

            // Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }

    }

    @Nested
    @DisplayName("Tests for Deleting User")
    public class DeleteUser {

        @Test
        @DisplayName("Should delete user with success when it exists")
        public void shouldDeleteUserWithSuccessWhenItExists() {
            // Arrange
            doReturn(true)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            doNothing()
                    .when(userRepository)
                    .deleteById(uuidArgumentCaptor.capture());
            
            var userId = UUID.randomUUID();

            // Act
            userService.deleteUser(userId.toString());

            // Assert
            var idList = uuidArgumentCaptor.getAllValues();

            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

        }
    }

}
