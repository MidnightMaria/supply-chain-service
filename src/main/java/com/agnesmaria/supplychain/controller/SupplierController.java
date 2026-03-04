package com.agnesmaria.supplychain.controller;

import com.agnesmaria.supplychain.model.Supplier;
import com.agnesmaria.supplychain.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "APIs for managing suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @Operation(summary = "Get all suppliers", description = "Retrieve a list of all suppliers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<Supplier> getAll() {
        return supplierService.getAll();
    }

    @Operation(summary = "Create new supplier", description = "Create a new supplier with unique code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created supplier"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or duplicate code")
    })
    @PostMapping
    public Supplier create(@RequestBody Supplier supplier) {
        return supplierService.create(supplier);
    }

    @Operation(summary = "Get supplier by ID", description = "Retrieve a specific supplier by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved supplier"),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @GetMapping("/{id}")
    public Supplier getById(@PathVariable Long id) {
        return supplierService.getById(id);
    }

    @Operation(summary = "Delete supplier", description = "Delete a supplier by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted supplier"),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        supplierService.delete(id);
    }

    @Operation(summary = "Update supplier", description = "Update an existing supplier's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated supplier"),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @PutMapping("/{id}")
    public Supplier update(@PathVariable Long id, @RequestBody Supplier supplier) {
        return supplierService.update(id, supplier);
    }
}