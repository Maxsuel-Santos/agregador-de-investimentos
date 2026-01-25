package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to associate a stock with an account")
public record AssociateAccountStockDto(

    @Schema(
        description = "Stock trading code (ticker) to be associated with the account",
        example = "VALE3"
    )
    String stockId,

    @Schema(
        description = "Number of shares to associate with the account",
        example = "10"
    )
    int quantity
) {
}