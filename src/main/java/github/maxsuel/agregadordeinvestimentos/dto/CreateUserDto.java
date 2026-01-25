package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload to create a new user")
public record CreateUserDto(

    @Schema(
        description = "Unique username used to identify the user in the system",
        example = "john_doe"
    )
    @NotBlank(message = "Username is required.")
    String username,

    @Schema(
        description = "Valid email address of the user",
        example = "john_doe@email.com"
    )
    @NotBlank(message = "Email is required.")
    @Email(message = "The email address must be valid.")
    String email,

    @Schema(
        description = "User password with a minimum of 8 characters",
        example = "password#123"
    )
    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, message = "The password must be at least 8 characters long.")
    String password
) {
}
