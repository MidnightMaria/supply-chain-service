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

    // 🔹 Get all purchase orders
    public List<PurchaseOrder> getAll() {
        return purchaseOrderRepository.findAll();
    }

    // 🔹 Get single purchase order by ID
    public PurchaseOrder getById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));
    }

    // 🔹 Create new purchase order
    @Transactional
    public PurchaseOrder create(PurchaseOrder order) {
        // Validasi supplier
        if (order.getSupplier() != null && order.getSupplier().getId() != null) {
            Supplier supplier = supplierRepository.findById(order.getSupplier().getId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            order.setSupplier(supplier);
        }

        // Set relasi dua arah untuk items
        if (order.getItems() != null) {
            for (PurchaseOrderItem item : order.getItems()) {
                item.setPurchaseOrder(order);
            }
        }

        log.info("🧾 Creating Purchase Order: {} with {} items",
                order.getOrderNumber(),
                (order.getItems() != null ? order.getItems().size() : 0));

        return purchaseOrderRepository.save(order);
    }

    // 🔹 Update PO status, dan trigger update stok otomatis
    @Transactional
    public PurchaseOrder update(Long id, PurchaseOrder updated) {
        PurchaseOrder existing = getById(id);
        existing.setStatus(updated.getStatus());

        // ✅ Sinkronkan item baru dari request (kalau dikirim ulang)
        if (updated.getItems() != null && !updated.getItems().isEmpty()) {
            existing.getItems().clear();
            for (PurchaseOrderItem newItem : updated.getItems()) {
                newItem.setPurchaseOrder(existing);
                existing.getItems().add(newItem);
            }
        }

        // ✅ Jika status = RECEIVED → otomatis update stok ke Inventory Service
        if ("RECEIVED".equalsIgnoreCase(updated.getStatus()) && existing.getItems() != null) {
            existing.getItems().forEach(item -> {
                try {
                    log.info("📦 Updating stock for SKU={} qty={} via Inventory Service",
                            item.getProductSku(), item.getQuantity());
                    inventoryClient.adjustStock(item.getProductSku(), item.getQuantity());

                    log.info("✅ Stock update success for SKU={} (+{} units)",
                            item.getProductSku(), item.getQuantity());

                } catch (Exception e) {
                    log.error("⚠️ Failed to update stock for SKU={}: {}", 
                            item.getProductSku(), e.getMessage());
                }
            });

            // (Optional) Auto-update jadi COMPLETED
            existing.setStatus("COMPLETED");
            log.info("🏁 Purchase Order {} marked as COMPLETED", existing.getOrderNumber());
        }

        return purchaseOrderRepository.save(existing);
    }

    // 🔹 Delete PO
    public void delete(Long id) {
        log.warn("🗑️ Deleting purchase order ID={}", id);
        purchaseOrderRepository.deleteById(id);
    }
}
