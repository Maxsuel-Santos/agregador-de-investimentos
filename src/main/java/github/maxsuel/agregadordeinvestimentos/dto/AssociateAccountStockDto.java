package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload to associate a stock with an account")
public record AssociateAccountStockDto(

    @Schema(
        description = "Stock trading code (ticker) to be associated with the account",
        example = "VALE3"
    )
    @NotBlank(message = "The stock code (stockId) is required.")
    String stockId,

    @Schema(
        description = "Number of shares to associate with the account",
        example = "10"
    )
    @Min(value = 1, message = "The minimum amount for membership is 1.")
    int quantity
) {
}
