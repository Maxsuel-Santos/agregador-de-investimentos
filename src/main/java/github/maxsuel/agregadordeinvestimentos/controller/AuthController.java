package github.maxsuel.agregadordeinvestimentos.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.maxsuel.agregadordeinvestimentos.dto.AuthResponseDto;
import github.maxsuel.agregadordeinvestimentos.dto.CreateUserDto;
import github.maxsuel.agregadordeinvestimentos.dto.LoginDto;
import github.maxsuel.agregadordeinvestimentos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Create/Register a new user.")
    @ApiResponse(responseCode = "201", description = "User created/registered successfully.")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody CreateUserDto createUserDto) {
        var registerResponse = authService.register(createUserDto);
        return ResponseEntity.created(URI.create("/users/" + registerResponse.user().userId())).body(registerResponse);
    }

    @Operation(summary = "Log in and receive the JWT token.",
            description = "Validates credentials and generates a Bearer token to authenticate subsequent requests.")
    @ApiResponses({
            @ApiResponse(
                responseCode = "200", 
                description = "Login successfully", 
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthResponseDto.class)
                )   
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "Invalid username or password",
                content = @Content
            )
    })
    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        var loginResponse = authService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

}
