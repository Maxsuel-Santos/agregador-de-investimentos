package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AssociateAccountStockDto(
        @Schema(description = "Code of the action to be linked", example = "VALE3")
        String stockId,
        @Schema(description = "Number of shares", example = "10")
        int quantity
) {

}