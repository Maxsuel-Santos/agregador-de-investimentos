package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response with user account details")
public record AccountResponseDto(

    @Schema(
        description = "Unique identifier of the account",
        example = "e87e1f43-8f0a-4a2b-8a8b-1234567890ab"
    )
    String accountId,

    @Schema(
        description = "Friendly description of the account",
        example = "Main account"
    )
    String description
) {
}
