package com.agnesmaria.supplychain.service;

import com.agnesmaria.supplychain.client.InventoryClient;
import com.agnesmaria.supplychain.model.PurchaseOrder;
import com.agnesmaria.supplychain.model.PurchaseOrderItem;
import com.agnesmaria.supplychain.model.Supplier;
import com.agnesmaria.supplychain.repository.PurchaseOrderRepository;
import com.agnesmaria.supplychain.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryClient inventoryClient;

    public List<PurchaseOrder> getAll() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));
    }

    @Transactional
    public PurchaseOrder create(PurchaseOrder order) {

        if (order.getSupplier() != null && order.getSupplier().getId() != null) {
            Supplier supplier = supplierRepository.findById(order.getSupplier().getId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));

            order.setSupplier(supplier);
        }

        if (order.getItems() != null) {
            for (PurchaseOrderItem item : order.getItems()) {
                item.setPurchaseOrder(order);
            }
        }

        log.info("Creating Purchase Order: {} with {} items",
                order.getOrderNumber(),
                (order.getItems() != null ? order.getItems().size() : 0));

        return purchaseOrderRepository.save(order);
    }

    @Transactional
    public PurchaseOrder update(Long id, PurchaseOrder updated) {

        PurchaseOrder existing = getById(id);

        existing.setStatus(updated.getStatus());

        if (updated.getItems() != null && !updated.getItems().isEmpty()) {
            existing.getItems().clear();

            for (PurchaseOrderItem newItem : updated.getItems()) {
                newItem.setPurchaseOrder(existing);
                existing.getItems().add(newItem);
            }
        }

        if ("RECEIVED".equalsIgnoreCase(updated.getStatus())
                && !"RECEIVED".equalsIgnoreCase(existing.getStatus())
                && !"COMPLETED".equalsIgnoreCase(existing.getStatus())
                && existing.getItems() != null) {

            existing.getItems().forEach(item -> {
                try {

                    log.info("Updating stock for SKU={} qty={}",
                            item.getProductSku(),
                            item.getQuantity());

                    inventoryClient.adjustStock(
                            item.getProductSku(),
                            item.getQuantity()
                    );

                    log.info("Stock update success for SKU={} (+{} units)",
                            item.getProductSku(),
                            item.getQuantity());

                } catch (Exception e) {

                    log.error("Failed to update stock for SKU={}: {}",
                            item.getProductSku(),
                            e.getMessage());
                }
            });

            existing.setStatus("COMPLETED");

            log.info("Purchase Order {} marked as COMPLETED",
                    existing.getOrderNumber());
        }

        return purchaseOrderRepository.save(existing);
    }

    public void delete(Long id) {
        log.warn("Deleting purchase order ID={}", id);
        purchaseOrderRepository.deleteById(id);
    }
    public long countAll() {
    return purchaseOrderRepository.count();
    }

    public long countPending() {
        return purchaseOrderRepository.countByStatus("PENDING");
    }

    public long countCompleted() {
        return purchaseOrderRepository.countByStatus("COMPLETED");
    }
}
