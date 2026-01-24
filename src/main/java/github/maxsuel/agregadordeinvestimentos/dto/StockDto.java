package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record StockDto(
        @Schema(example = "Petrobras PN")
        String shortName,
        @Schema(description = "Current market price", example = "38.50")
        double regularMarketPrice,
        @Schema(example = "BRL")
        String currency,
        @Schema(example = "https://icons.brapi.dev/icons/PETR4.svg")
        String logourl
) {

}
