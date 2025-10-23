package com.agnesmaria.supplychain.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class InventoryClient {

    private final RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    public void adjustStock(String productSku, int quantity) {
        String url = inventoryServiceUrl + "/api/inventory/adjust";
        Map<String, Object> request = Map.of(
                "productSku", productSku,
                "quantity", quantity,
                "operation", "IN"  // tambah stok
        );

        restTemplate.postForObject(url, request, Void.class);
    }
}
