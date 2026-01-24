package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AccountStockResponseDto(
        @Schema(example = "ITUB4")
        String stockId,
        @Schema(example = "Itaú Unibanco")
        String name,
        @Schema(example = "100")
        int quantity,
        @Schema(example = "32.45")
        double price,
        @Schema(description = "Total amount invested (quantity * price)", example = "3245.00")
        double total,
        @Schema(example = "https://icons.brapi.dev/icons/ITUB4.svg")
        String logoUrl
) {

}
