package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stock market data returned by the external Brapi API")
public record StockDto(

    @Schema(
        description = "Short display name of the company or stock",
        example = "Petrobras PN"
    )
    String shortName,

    @Schema(
        description = "Current market price of the stock",
        example = "38.50"
    )
    double regularMarketPrice,

    @Schema(
        description = "Currency used for the stock price",
        example = "BRL"
    )
    String currency,

    @Schema(
        description = "URL of the company logo",
        example = "https://icons.brapi.dev/icons/PETR4.svg"
    )
    String logourl
) {
}
