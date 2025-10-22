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
    private final PurchaseOrderService poService;

    @GetMapping
    public List<PurchaseOrder> getAll() {
        return poService.getAll();
    }

    @PostMapping
    public PurchaseOrder create(@RequestBody PurchaseOrder po) {
        return poService.create(po);
    }
}
