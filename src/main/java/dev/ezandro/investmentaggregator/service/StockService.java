package dev.ezandro.investmentaggregator.service;

import dev.ezandro.investmentaggregator.dto.CreateStockDTO;
import dev.ezandro.investmentaggregator.entity.Stock;
import dev.ezandro.investmentaggregator.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDTO createStockDTO) {
        var stock = new Stock(createStockDTO.stockId(), createStockDTO.description());
        this.stockRepository.save(stock);
    }
}