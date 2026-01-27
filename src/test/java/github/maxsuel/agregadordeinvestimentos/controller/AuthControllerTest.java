package github.maxsuel.agregadordeinvestimentos.controller;

import github.maxsuel.agregadordeinvestimentos.dto.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.dto.LoginDto;
import github.maxsuel.agregadordeinvestimentos.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
            when(authService.register(dto)).thenReturn(userId);

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
            var mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."; // Fake token

            when(authService.login(loginDto)).thenReturn(mockToken);

            // Act
            var response = authController.login(loginDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(mockToken, response.getBody());

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
