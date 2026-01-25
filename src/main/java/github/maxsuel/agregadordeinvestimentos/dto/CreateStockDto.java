package github.maxsuel.agregadordeinvestimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload to create a new stock")
public record CreateStockDto(

    @Schema(
        description = "Stock trading code (ticker)",
        example = "PETR4"
    )
    @NotBlank(message = "The Stock Trading Code is mandatory.")
    @Size(min = 4, max = 10)
    String stockId,

    @Schema(
        description = "Company legal or trade name",
        example = "Petróleo Brasileiro S.A."
    )
    @NotBlank(message = "Description cannot be empty.")
    String description
) {
}
