package github.maxsuel.agregadordeinvestimentos.controller;

import java.util.List;

import github.maxsuel.agregadordeinvestimentos.dto.AccountBalanceDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.maxsuel.agregadordeinvestimentos.dto.AccountStockResponseDto;
import github.maxsuel.agregadordeinvestimentos.dto.AssociateAccountStockDto;
import github.maxsuel.agregadordeinvestimentos.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(
    name = "Accounts", 
    description = "Endpoints for portfolio management and stock association."
)
public class AccountController {

    private final AccountService accountService;

    @Operation(
        summary = "Associates an stock with an account.", 
        description = "Links a financial asset and the quantity purchased to a specific account."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Stock associated with success.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Account or Stock not found.",
            content = @Content
        )
    })
    @PostMapping(path = "/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(
        @PathVariable("accountId") String accountId,
        @Valid @RequestBody AssociateAccountStockDto dto
    ) {
        accountService.associateStock(accountId, dto);

        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "List of portfolio shares.", 
        description = "Returns all assets of an account with prices updated in real time via the Brapi API."
    )
    @ApiResponse(
        responseCode = "200", 
        description = "List returned successfully.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(
                schema = @Schema(implementation = AccountStockResponseDto.class)
            )
        )
    )
    @GetMapping(path = "/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> listAllStocks(@PathVariable("accountId") String accountId) {
        var stocks = accountService.listAllStocks(accountId);

        return ResponseEntity.ok(stocks);
    }

    @Operation(summary = "Get account total balance", description = "Calculates the sum of all stocks in the account based on real-time Brapi prices.")
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<AccountBalanceDto> getBalance(@PathVariable String accountId) {
        return ResponseEntity.ok(accountService.getAccountBalance(accountId));
    }

}
