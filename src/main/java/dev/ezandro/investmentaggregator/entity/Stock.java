package dev.ezandro.investmentaggregator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_stocks")
public class Stock {
    @Id
    @Column(name = "stock_id")
    private String stockId;

    @Column(name = "description")
    private String description;

    public Stock() {
    }

    public Stock(String stockId, String description) {
        this.stockId = stockId;
        this.description = description;
    }

    public String getStockId() {
        return this.stockId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}