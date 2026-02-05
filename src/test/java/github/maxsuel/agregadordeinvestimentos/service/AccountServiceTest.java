package github.maxsuel.agregadordeinvestimentos.service;

import github.maxsuel.agregadordeinvestimentos.client.BrapiClient;
import github.maxsuel.agregadordeinvestimentos.dto.external.brapi.BrapiResponseDto;
import github.maxsuel.agregadordeinvestimentos.dto.external.brapi.StockDto;
import github.maxsuel.agregadordeinvestimentos.mapper.AccountStockMapper;
import github.maxsuel.agregadordeinvestimentos.repository.AccountRepository;
import github.maxsuel.agregadordeinvestimentos.repository.AccountStockRepository;
import github.maxsuel.agregadordeinvestimentos.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private AccountStockRepository accountStockRepository;

    @Mock
    private BrapiClient brapiClient;

    @Mock
    private AccountStockMapper accountStockMapper;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(
                accountRepository,
                stockRepository,
                accountStockRepository,
                brapiClient,
                accountStockMapper
        );

        ReflectionTestUtils.setField(accountService, "TOKEN", "test-token");
    }

    @Test
    @DisplayName("Should calculate account balance correctly using StockDto")
    void getAccountBalance_Success() {
        var accountId = UUID.randomUUID();
        var stockId = "PETR4";
        var quantity = 10;
        var marketPrice = 30.0;

        var user = new github.maxsuel.agregadordeinvestimentos.entity.User();
        user.setUsername("Alice");

        var account = new github.maxsuel.agregadordeinvestimentos.entity.Account(
                user,
                "Investment Portfolio",
                new java.util.ArrayList<>()
        );
        account.setAccountId(accountId);

        var stock = new github.maxsuel.agregadordeinvestimentos.entity.Stock();
        stock.setStockId(stockId);

        var accountStockId = new github.maxsuel.agregadordeinvestimentos.entity.AccountStockId(accountId, stockId);
        var accountStock = new github.maxsuel.agregadordeinvestimentos.entity.AccountStock(
                accountStockId,
                account,
                stock,
                quantity
        );

        account.getAccountStocks().add(accountStock);

        var stockDto = new StockDto(
                "PETR4",
                "Petrobras PN",
                "Petroleo Brasileiro SA Pfd",
                marketPrice,
                "BRL",
                "https://icons.brapi.dev/icons/PETR4.svg"
        );

        var brapiResponse = new BrapiResponseDto(List.of(stockDto));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(brapiClient.getQuote(any(), eq(stockId))).thenReturn(brapiResponse);
        when(accountStockMapper.calculateTotal(anyDouble(), anyDouble())).thenReturn(300.0);

        var result = accountService.getAccountBalance(accountId.toString());

        assertNotNull(result);
        assertEquals(300.0, result.totalBalance());
    }
}
