package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateAccountDto(
        @Schema(description = "Account description", example = "Dividend Portfolio")
        String description,
        @Schema(example = "Avenida Paulista")
        String street,
        @Schema(example = "1500")
        Integer number
) {

}
