package github.maxsuel.agregadordeinvestimentos.service;

import github.maxsuel.agregadordeinvestimentos.config.TokenService;
import github.maxsuel.agregadordeinvestimentos.dto.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.dto.LoginDto;
import github.maxsuel.agregadordeinvestimentos.entity.User;
import github.maxsuel.agregadordeinvestimentos.entity.enums.Role;
import github.maxsuel.agregadordeinvestimentos.exceptions.UserNotFoundException;
import github.maxsuel.agregadordeinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Nested
    @DisplayName("Tests for Register")
    public class RegisterTests {

        @Test
        @DisplayName("Should register user with hashed password")
        public void shouldRegisterUserWithSuccess() {
            // Arrange
            var dto = new CreateUserDto("username", "user@email.com", "plainPassword");
            var hashedPassword = "hashedPassword123";
            var user = new User("User", "user@email.com", hashedPassword, Role.ADMIN);
            var userId = UUID.randomUUID();
            user.setUserId(userId);

            when(passwordEncoder.encode(dto.password())).thenReturn(hashedPassword);
            when(userRepository.save(any(User.class))).thenReturn(user);

            // Act
            var result = authService.register(dto);

            // Assert
            assertNotNull(result);
            assertEquals(userId, result);

            verify(passwordEncoder).encode("plainPassword");
            verify(userRepository).save(any(User.class));
        }

    }

    @Nested
    @DisplayName("Tests for Login")
    class LoginTests {

        @Test
        @DisplayName("Should login and return token when credentials are valid")
        void shouldLoginWithSuccess() {
            // Arrange
            var dto = new LoginDto("user", "plainPassword");
            var user = new User("user", "user@email.com", "hashedPassword", Role.ADMIN);
            var expectedToken = "token-jwt-123";

            when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
            when(tokenService.generateToken(user)).thenReturn(expectedToken);

            // Act
            var result = authService.login(dto);

            // Assert
            assertEquals(expectedToken, result);
            verify(tokenService).generateToken(user);
        }

        @Test
        @DisplayName("Should throw BadCredentialsException when password does not match")
        void shouldThrowExceptionWhenPasswordInvalid() {
            // Arrange
            var dto = new LoginDto("user", "wrongPassword");
            var user = new User("user", "user@email.com", "hashedPassword", Role.ADMIN);

            when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(false);

            // Act & Assert
            assertThrows(BadCredentialsException.class, () -> authService.login(dto));
            verify(tokenService, never()).generateToken(any());
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user does not exist")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            var dto = new LoginDto("nonExistent", "anyPass");
            when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> authService.login(dto));
        }
    }

}
