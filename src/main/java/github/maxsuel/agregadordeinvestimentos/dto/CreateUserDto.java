package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to create a new user")
public record CreateUserDto(

    @Schema(
        description = "Unique username used to identify the user in the system",
        example = "john_doe"
    )
    String username,

    @Schema(
        description = "Valid email address of the user",
        example = "john_doe@email.com"
    )
    String email,

    @Schema(
        description = "User password with a minimum of 8 characters",
        example = "senha#123"
    )
    String password
) {
}
