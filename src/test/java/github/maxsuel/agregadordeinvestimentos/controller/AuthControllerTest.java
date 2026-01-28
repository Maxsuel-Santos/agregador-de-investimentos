package github.maxsuel.agregadordeinvestimentos.controller;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import github.maxsuel.agregadordeinvestimentos.dto.AuthResponseDto;
import github.maxsuel.agregadordeinvestimentos.dto.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.dto.LoginDto;
import github.maxsuel.agregadordeinvestimentos.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Nested
    @DisplayName("Tests for Create/Register User")
    public class CreateUser {

        @Test
        @DisplayName("Should return 201 Created and Location header on success")
        public void shouldCreateUserWithSuccess() {
            // Arrange
            var dto = new CreateUserDto("username", "username@email.com", "123");
            var userId = UUID.randomUUID();
            var userResponse = new AuthResponseDto("fake-jwt-token", null); // Fake token and user
            when(authService.register(dto)).thenReturn(userResponse);

            // Act
            var response = authController.register(dto);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getHeaders().getLocation());
            assertTrue(response.getHeaders().getLocation().getPath().contains(userId.toString()));
        }
    }

    @Nested
    @DisplayName("Tests for Login")
    public class Login {

        @Test
        @DisplayName("Should return 200 OK and JWT token on success")
        public void shouldLoginWithSuccess() {
            // Arrange
            var loginDto = new LoginDto("username", "123");
            var userResponse = new AuthResponseDto("fake-jwt-token", null); // Fake token and user

            when(authService.login(loginDto)).thenReturn(userResponse);

            // Act
            var response = authController.login(loginDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(userResponse, response.getBody());

            verify(authService, times(1)).login(loginDto);
        }

        @Test
        @DisplayName("Should propagate exception when credentials are invalid")
        public void shouldThrowExceptionWhenLoginFails() {
            // Arrange
            var loginDto = new LoginDto("wrongUser", "wrongPass");

            when(authService.login(loginDto))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            // Act & Assert
            assertThrows(BadCredentialsException.class, () -> authController.login(loginDto));
        }
    }

}
