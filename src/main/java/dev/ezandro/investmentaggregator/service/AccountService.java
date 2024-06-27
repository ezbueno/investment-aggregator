package dev.ezandro.investmentaggregator.service;

import dev.ezandro.investmentaggregator.dto.AccountStockResponseDTO;
import dev.ezandro.investmentaggregator.dto.AssociateAccountStockDTO;
import dev.ezandro.investmentaggregator.entity.AccountStock;
import dev.ezandro.investmentaggregator.entity.AccountStockId;
import dev.ezandro.investmentaggregator.repository.AccountRepository;
import dev.ezandro.investmentaggregator.repository.AccountStockRepository;
import dev.ezandro.investmentaggregator.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountStockRepository accountStockRepository;
    private final StockRepository stockRepository;

    public AccountService(AccountRepository accountRepository, AccountStockRepository accountStockRepository, StockRepository stockRepository) {
        this.accountRepository = accountRepository;
        this.accountStockRepository = accountStockRepository;
        this.stockRepository = stockRepository;
    }

    public void associateStock(String accountId, AssociateAccountStockDTO associateAccountStockDTO) {
        var account = this.accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = this.stockRepository.findById(associateAccountStockDTO.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var accountStockId = new AccountStockId(account.getAccountId(), stock.getStockId());
        var accountStock = new AccountStock(accountStockId, account, stock, associateAccountStockDTO.quantity());

        this.accountStockRepository.save(accountStock);
    }

    public List<AccountStockResponseDTO> listStocks(String accountId) {
        var account = this.accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(accountStock -> new AccountStockResponseDTO(accountStock.getStock().getStockId(), accountStock.getQuantity(), 0.0))
                .toList();
    }
}