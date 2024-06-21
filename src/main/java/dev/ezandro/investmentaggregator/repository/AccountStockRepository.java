package dev.ezandro.investmentaggregator.repository;

import dev.ezandro.investmentaggregator.entity.AccountStock;
import dev.ezandro.investmentaggregator.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}