package dev.ezandro.investmentaggregator.service;

import dev.ezandro.investmentaggregator.client.BrapiClient;
import dev.ezandro.investmentaggregator.dto.AccountStockResponseDTO;
import dev.ezandro.investmentaggregator.dto.AssociateAccountStockDTO;
import dev.ezandro.investmentaggregator.entity.AccountStock;
import dev.ezandro.investmentaggregator.entity.AccountStockId;
import dev.ezandro.investmentaggregator.repository.AccountRepository;
import dev.ezandro.investmentaggregator.repository.AccountStockRepository;
import dev.ezandro.investmentaggregator.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Value("#{environment.token}")
    private String token;

    private final AccountRepository accountRepository;
    private final AccountStockRepository accountStockRepository;
    private final StockRepository stockRepository;
    private final BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository,
                          AccountStockRepository accountStockRepository,
                          StockRepository stockRepository,
                          BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.accountStockRepository = accountStockRepository;
        this.stockRepository = stockRepository;
        this.brapiClient = brapiClient;
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
                .map(accountStock -> new AccountStockResponseDTO(
                        accountStock.getStock().getStockId(),
                        accountStock.getQuantity(),
                        this.getTotal(accountStock.getQuantity(), accountStock.getStock().getStockId())))
                .toList();
    }

    private double getTotal(Integer quantity, String stockId) {
        var response = this.brapiClient.getQuote(this.token, stockId);
        var price = response.results().getFirst().regularMarketPrice();
        return quantity * price;
    }
}