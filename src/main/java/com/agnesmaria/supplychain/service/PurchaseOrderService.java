package com.agnesmaria.supplychain.service;

import com.agnesmaria.supplychain.client.InventoryClient;
import com.agnesmaria.supplychain.model.PurchaseOrder;
import com.agnesmaria.supplychain.model.PurchaseOrderItem;
import com.agnesmaria.supplychain.model.Supplier;
import com.agnesmaria.supplychain.repository.PurchaseOrderRepository;
import com.agnesmaria.supplychain.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

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

    public PurchaseOrder create(PurchaseOrder order) {
        // 🔹 Validasi Supplier
        if (order.getSupplier() != null && order.getSupplier().getId() != null) {
            Supplier supplier = supplierRepository.findById(order.getSupplier().getId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            order.setSupplier(supplier);
        }

        // 🔹 Set relasi 2 arah untuk item
        if (order.getItems() != null) {
            for (PurchaseOrderItem item : order.getItems()) {
                item.setPurchaseOrder(order);
            }
        }

        return purchaseOrderRepository.save(order);
    }

    public PurchaseOrder update(Long id, PurchaseOrder updated) {
        PurchaseOrder existing = getById(id);
        existing.setStatus(updated.getStatus());

        // 🔹 Jika status berubah jadi RECEIVED, update stok otomatis ke inventory-service
        if ("RECEIVED".equalsIgnoreCase(updated.getStatus()) && existing.getItems() != null) {
            existing.getItems().forEach(item -> {
                try {
                    inventoryClient.adjustStock(item.getProductSku(), item.getQuantity());
                    System.out.printf("✅ Stok %s bertambah %d unit di inventory-service%n",
                            item.getProductSku(), item.getQuantity());
                } catch (Exception e) {
                    System.err.printf("⚠️ Gagal update stok untuk %s: %s%n",
                            item.getProductSku(), e.getMessage());
                }
            });
        }

        return purchaseOrderRepository.save(existing);
    }

    public void delete(Long id) {
        purchaseOrderRepository.deleteById(id);
    }
}
