package com.agnesmaria.supplychain.service;

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

    public List<PurchaseOrder> getAll() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));
    }

    public PurchaseOrder create(PurchaseOrder order) {
        // 🔹 Ambil supplier berdasarkan ID dari database
        if (order.getSupplier() != null && order.getSupplier().getId() != null) {
            Supplier supplier = supplierRepository.findById(order.getSupplier().getId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            order.setSupplier(supplier);
        }

        // 🔹 Pastikan setiap item punya relasi balik ke order
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setPurchaseOrder(order));
        }

        return purchaseOrderRepository.save(order);
    }

    public PurchaseOrder update(Long id, PurchaseOrder updated) {
        PurchaseOrder existing = getById(id);
        existing.setStatus(updated.getStatus());
        existing.setItems(updated.getItems());
        if (existing.getItems() != null) {
            existing.getItems().forEach(item -> item.setPurchaseOrder(existing));
        }
        return purchaseOrderRepository.save(existing);
    }

    public void delete(Long id) {
        purchaseOrderRepository.deleteById(id);
    }
}
