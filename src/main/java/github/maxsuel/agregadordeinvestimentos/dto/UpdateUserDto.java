package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to update user information")
public record UpdateUserDto(

    @Schema(
        description = "New username to replace the current one",
        example = "novo_usuario"
    )
    String username,

    @Schema(
        description = "New user password with a minimum of 8 characters",
        example = "NovaSenha@2026"
    )
    String password
) {
}

