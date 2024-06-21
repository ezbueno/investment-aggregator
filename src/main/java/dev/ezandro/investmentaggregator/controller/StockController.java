package dev.ezandro.investmentaggregator.controller;

import dev.ezandro.investmentaggregator.dto.CreateStockDTO;
import dev.ezandro.investmentaggregator.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<Void> createStock(@RequestBody CreateStockDTO createStockDTO) {
        this.stockService.createStock(createStockDTO);
        return ResponseEntity.ok().build();
    }
}