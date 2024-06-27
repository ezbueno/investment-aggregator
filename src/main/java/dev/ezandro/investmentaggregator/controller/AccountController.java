package dev.ezandro.investmentaggregator.controller;

import dev.ezandro.investmentaggregator.dto.AccountStockResponseDTO;
import dev.ezandro.investmentaggregator.dto.AssociateAccountStockDTO;
import dev.ezandro.investmentaggregator.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable(value = "accountId") String accountId,
                                               @RequestBody AssociateAccountStockDTO associateAccountStockDTO) {
        this.accountService.associateStock(accountId, associateAccountStockDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDTO>> listStocks(@PathVariable(value = "accountId") String accountId) {
        return ResponseEntity.ok(this.accountService.listStocks(accountId));
    }
}