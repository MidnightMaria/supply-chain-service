package com.agnesmaria.supplychain.controller;

import com.agnesmaria.supplychain.model.PurchaseOrder;
import com.agnesmaria.supplychain.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
@Tag(name = "Purchase Orders", description = "APIs for managing purchase orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @Operation(summary = "Get all purchase orders", description = "Retrieve a list of all purchase orders")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<PurchaseOrder> getAll() {
        return purchaseOrderService.getAll();
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("total", purchaseOrderService.countAll());
        stats.put("pending", purchaseOrderService.countPending());
        stats.put("completed", purchaseOrderService.countCompleted());

        return stats;
    }

    @Operation(summary = "Get purchase order by ID", description = "Retrieve a specific purchase order by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved purchase order"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found")
    })
    @GetMapping("/{id}")
    public PurchaseOrder getById(@PathVariable Long id) {
        return purchaseOrderService.getById(id);
    }

    @Operation(summary = "Create new purchase order", description = "Create a new purchase order with items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created purchase order"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public PurchaseOrder create(@RequestBody PurchaseOrder order) {
        return purchaseOrderService.create(order);
    }

    @Operation(summary = "Update purchase order", description = "Update an existing purchase order and trigger stock update when status is RECEIVED")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated purchase order"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found")
    })
    @PutMapping("/{id}")
    public PurchaseOrder update(@PathVariable Long id, @RequestBody PurchaseOrder order) {
        return purchaseOrderService.update(id, order);
    }

    @Operation(summary = "Delete purchase order", description = "Delete a purchase order by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted purchase order"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        purchaseOrderService.delete(id);
    }
}