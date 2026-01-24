package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AccountResponseDto(
        @Schema(example = "e87e1f43-8f0a-4a2b-8a8b-1234567890ab")
        String accountId,
        @Schema(example = "My Main Account")
        String description
) {

}
