package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to create a new account")
public record CreateAccountDto(

    @Schema(
        description = "Human-readable description of the account",
        example = "Carteira de dividendos"
    )
    String description,

    @Schema(
        description = "Street name of the account address",
        example = "Avenida Paulista"
    )
    String street,

    @Schema(
        description = "Street number of the account address",
        example = "1500"
    )
    Integer number
) {
}
