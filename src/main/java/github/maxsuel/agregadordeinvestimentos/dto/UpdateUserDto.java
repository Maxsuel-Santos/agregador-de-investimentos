package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserDto(
        @Schema(example = "new_username")
        String username,
        @Schema(example = "NewPassword@2026")
        String password
) {

}
