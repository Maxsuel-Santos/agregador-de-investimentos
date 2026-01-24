package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateUserDto(
        @Schema(description = "Unique username", example = "username_invest")
        String username,

        @Schema(description = "Valid user email address", example = "user@email.com")
        String email,

        @Schema(description = "Strong password (minimum 8 characters)", example = "pass#123")
        String password
) {

}
