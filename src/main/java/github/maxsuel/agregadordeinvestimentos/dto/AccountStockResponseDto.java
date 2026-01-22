package github.maxsuel.agregadordeinvestimentos.dto;

public record AccountStockResponseDto(
        String stockId,
        String name,
        int quantity,
        double price,
        double total,
        String logoUrl
) {

}
