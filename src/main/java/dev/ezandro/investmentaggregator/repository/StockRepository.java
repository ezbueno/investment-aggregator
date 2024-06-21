package dev.ezandro.investmentaggregator.repository;

import dev.ezandro.investmentaggregator.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, String> {
}