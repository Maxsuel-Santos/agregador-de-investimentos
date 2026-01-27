package github.maxsuel.agregadordeinvestimentos.controller;

import github.maxsuel.agregadordeinvestimentos.dto.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.dto.LoginDto;
import github.maxsuel.agregadordeinvestimentos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Create/Register a new user.")
    @ApiResponse(responseCode = "201", description = "User created/registered successfully.")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody CreateUserDto createUserDto) {
        var userId = authService.register(createUserDto);
        return ResponseEntity.created(URI.create("/users/" + userId.toString())).build();
    }

    @Operation(summary = "Log in and receive the JWT token.",
            description = "Validates credentials and generates a Bearer token to authenticate subsequent requests.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {
        var token = authService.login(loginDto);
        return ResponseEntity.ok(token);
    }

}
