package dev.ezandro.investmentaggregator.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_accounts_stocks")
public class AccountStock {
    @EmbeddedId
    private AccountStockId accountStockId;

    @ManyToOne
    @MapsId(value = "accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId(value = "stockId")
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column(name = "quantity")
    private Integer quantity;

    public AccountStock() {
    }

    public AccountStock(AccountStockId accountStockId, Account account, Stock stock, Integer quantity) {
        this.accountStockId = accountStockId;
        this.account = account;
        this.stock = stock;
        this.quantity = quantity;
    }

    public AccountStockId getAccountStockId() {
        return this.accountStockId;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}