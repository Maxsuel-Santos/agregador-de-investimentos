package github.maxsuel.agregadordeinvestimentos.dto;

public record StockDto(String shortName,
                       double regularMarketPrice,
                       String currency,
                       String logourl
) {

}
