package github.maxsuel.agregadordeinvestimentos.controller;

import github.maxsuel.agregadordeinvestimentos.dto.AccountStockResponseDto;
import github.maxsuel.agregadordeinvestimentos.dto.AssociateAccountStockDto;
import github.maxsuel.agregadordeinvestimentos.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Endpoints for portfolio management and stock association.")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Associates an stock with an account.", description = "Links a financial asset and the quantity purchased to a specific account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock associated with success."),
            @ApiResponse(responseCode = "404", description = "Account or Stock not found.")
    })
    @PostMapping(path = "/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId,
                                               @Valid @RequestBody AssociateAccountStockDto dto) {
        accountService.associateStock(accountId, dto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "List of portfolio shares.", description = "Returns all assets of an account with prices updated in real time via the Brapi API.")
    @ApiResponse(responseCode = "200", description = "List returned successfully.")
    @GetMapping(path = "/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> listAllStocks(@PathVariable("accountId") String accountId) {
        var stocks = accountService.listAllStocks(accountId);

        return ResponseEntity.ok(stocks);
    }

}
