package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginDto(
        @Schema(example = "investor_name", description = "Registered username")
        String username,
        @Schema(example = "pass123", description = "Password in plain text")
        String password
) {

}