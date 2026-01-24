package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record BrapiResponseDto(
        @Schema(description = "List of results returned by the external API.")
        List<StockDto> results
) {

}
