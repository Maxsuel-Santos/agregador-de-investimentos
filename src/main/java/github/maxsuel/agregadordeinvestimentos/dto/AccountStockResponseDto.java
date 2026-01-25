package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a stock position in the user's portfolio")
public record AccountStockResponseDto(

    @Schema(
        description = "Stock ticker symbol",
        example = "ITUB4"
    )
    String stockId,

    @Schema(
        description = "Name of the company issuing the stock",
        example = "Itaú Unibanco"
    )
    String name,

    @Schema(
        description = "Total quantity of the stock in the portfolio",
        example = "100"
    )
    int quantity,

    @Schema(
        description = "Average price paid per unit of the stock",
        example = "32.45"
    )
    double price,

    @Schema(
        description = "Total amount invested in the stock (quantity x average price)",
        example = "3245.00"
    )
    double total,

    @Schema(
        description = "URL of the logo of the company issuing the stock",
        example = "https://icons.brapi.dev/icons/ITUB4.svg"
    )
    String logoUrl
) {
}
