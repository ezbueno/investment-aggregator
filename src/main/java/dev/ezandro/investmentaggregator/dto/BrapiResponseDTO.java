package dev.ezandro.investmentaggregator.dto;

import java.util.List;

public record BrapiResponseDTO(List<StockDTO> results) {
}
