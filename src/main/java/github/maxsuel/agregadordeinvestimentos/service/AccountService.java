package github.maxsuel.agregadordeinvestimentos.service;

import github.maxsuel.agregadordeinvestimentos.client.BrapiClient;
import github.maxsuel.agregadordeinvestimentos.dto.AccountStockResponseDto;
import github.maxsuel.agregadordeinvestimentos.dto.AssociateAccountStockDto;
import github.maxsuel.agregadordeinvestimentos.entity.AccountStock;
import github.maxsuel.agregadordeinvestimentos.entity.AccountStockId;
import github.maxsuel.agregadordeinvestimentos.repository.AccountRepository;
import github.maxsuel.agregadordeinvestimentos.repository.AccountStockRepository;
import github.maxsuel.agregadordeinvestimentos.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Value("#{environment.TOKEN}")
    private String TOKEN; // Brapi API token

    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;
    private final BrapiClient brapiClient;

    public void associateStock(String accountId, AssociateAccountStockDto dto) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND)));

        var stock = stockRepository.findById(dto.stockId())
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND)));

        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
                id,
                account,
                stock,
                dto.quantity()
        );

        accountStockRepository.save(entity);
    }

    public List<AccountStockResponseDto> listAllStocks(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(as -> {
                    var response = brapiClient.getQuote(TOKEN, as.getStock().getStockId());
                    var stockInfo = response.results().getFirst();

                    double price = stockInfo.regularMarketPrice();
                    double totalRaw = as.getQuantity() * price;

                    double totalRounded = BigDecimal.valueOf(totalRaw)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();

                    return new AccountStockResponseDto(
                            as.getStock().getStockId(),
                            stockInfo.shortName(),
                            as.getQuantity(),
                            price,
                            totalRounded,
                            stockInfo.logourl()
                    );
                })
                .toList();
    }
}
