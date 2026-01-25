package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to create a new stock")
public record CreateStockDto(

    @Schema(
        description = "Stock trading code (ticker)",
        example = "PETR4"
    )
    String stockId,

    @Schema(
        description = "Company legal or trade name",
        example = "Petróleo Brasileiro S.A."
    )
    String description
) {
}
