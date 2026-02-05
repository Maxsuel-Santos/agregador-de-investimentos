package github.maxsuel.agregadordeinvestimentos.service;

import github.maxsuel.agregadordeinvestimentos.client.BrapiClient;
import github.maxsuel.agregadordeinvestimentos.dto.response.account.AccountBalanceDto;
import github.maxsuel.agregadordeinvestimentos.dto.response.account.AccountStockResponseDto;
import github.maxsuel.agregadordeinvestimentos.dto.request.account.AssociateAccountStockDto;
import github.maxsuel.agregadordeinvestimentos.entity.AccountStock;
import github.maxsuel.agregadordeinvestimentos.entity.AccountStockId;
import github.maxsuel.agregadordeinvestimentos.exceptions.AccountNotFoundException;
import github.maxsuel.agregadordeinvestimentos.mapper.AccountStockMapper;
import github.maxsuel.agregadordeinvestimentos.repository.AccountRepository;
import github.maxsuel.agregadordeinvestimentos.repository.AccountStockRepository;
import github.maxsuel.agregadordeinvestimentos.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Value("${BRAPI_TOKEN}")
    private String TOKEN; // Brapi API token

    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;
    private final BrapiClient brapiClient;
    private final AccountStockMapper accountStockMapper;

    @Transactional
    public void associateStock(String accountId, @NonNull AssociateAccountStockDto dto) {
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

    @CircuitBreaker(name = "brapiService", fallbackMethod = "fallbackListStocks")
    public List<AccountStockResponseDto> listAllStocks(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(as -> {
                    var response = brapiClient.getQuote(TOKEN, as.getStock().getStockId());
                    var stockInfo = response.results().getFirst();

                    return accountStockMapper.toDto(as, stockInfo);
                })
                .toList();
    }

    public List<AccountStockResponseDto> fallbackListStocks(String accountId, Throwable t) {
        return List.of(new AccountStockResponseDto(
                "N/A", "Service unavailable", "Service unavailable", 0, 0.0, 0.0, ""
        ));
    }

    public AccountBalanceDto getAccountBalance(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));

        if (account.getAccountStocks().isEmpty()) {
            return new AccountBalanceDto(accountId, 0.0, Instant.now());
        }

        String tickers = account.getAccountStocks().stream()
                .map(as -> as.getStock().getStockId())
                .collect(Collectors.joining(","));

        var response = brapiClient.getQuote(TOKEN, tickers);

        double total = account.getAccountStocks().stream()
                .mapToDouble(as -> {
                    var stockData = response.results().stream()
                            .filter(r -> r != null && as.getStock().getStockId().equals(r.stock()))
                            .findFirst()
                            .orElseThrow();
                    return as.getQuantity() * stockData.regularMarketPrice();
                })
                .sum();

        double roundedTotal = accountStockMapper.calculateTotal(1.0, total);

        return new AccountBalanceDto(accountId, roundedTotal, Instant.now());
    }
}
