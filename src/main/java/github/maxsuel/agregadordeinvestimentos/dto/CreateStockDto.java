package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateStockDto(
        @Schema(description = "Stock ticker", example = "PETR4")
        String stockId,
        @Schema(description = "Company name", example = "Petróleo Brasileiro S.A.")
        String description
) {

}
