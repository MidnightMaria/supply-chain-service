package com.agnesmaria.supplychain.controller;

import com.agnesmaria.supplychain.model.PurchaseOrder;
import com.agnesmaria.supplychain.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @GetMapping
    public List<PurchaseOrder> getAll() {
        return purchaseOrderService.getAll();
    }

    @GetMapping("/{id}")
    public PurchaseOrder getById(@PathVariable Long id) {
        return purchaseOrderService.getById(id);
    }

    @PostMapping
    public PurchaseOrder create(@RequestBody PurchaseOrder order) {
        return purchaseOrderService.create(order);
    }

    @PutMapping("/{id}")
    public PurchaseOrder update(@PathVariable Long id, @RequestBody PurchaseOrder order) {
        return purchaseOrderService.update(id, order);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        purchaseOrderService.delete(id);
    }
}
