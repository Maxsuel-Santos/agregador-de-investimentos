package github.maxsuel.agregadordeinvestimentos.service;

import github.maxsuel.agregadordeinvestimentos.client.BrapiClient;
import github.maxsuel.agregadordeinvestimentos.dto.BrapiResponseDto;
import github.maxsuel.agregadordeinvestimentos.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private BrapiClient brapiClient;

    @InjectMocks
    private AccountService accountService;

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

        var stockDto = new github.maxsuel.agregadordeinvestimentos.dto.StockDto(
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

        var result = accountService.getAccountBalance(accountId.toString());

        assertNotNull(result);
        assertEquals(accountId.toString(), result.accountId());
        assertEquals(300.0, result.totalBalance()); // 10 * 30.0

        verify(accountRepository, times(1)).findById(accountId);
        verify(brapiClient, times(1)).getQuote(any(), eq(stockId));
    }
}
