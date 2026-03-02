package br.com.devsuperior.car_dealer.batch.domain;

import java.math.BigDecimal;

// Representa uma venda lida do CSV.
public record SaleRecord(
        String dealerId,
        String saleDate,
        String model,
        String paymentType,
        BigDecimal salePriceBrl
) {
}
