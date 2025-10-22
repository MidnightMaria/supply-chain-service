package com.agnesmaria.supplychain.service;

import com.agnesmaria.supplychain.model.Supplier;
import com.agnesmaria.supplychain.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    public Supplier create(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier getById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }
    
    public Supplier update(Long id, Supplier updatedSupplier) {
    Supplier existing = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));

    existing.setCode(updatedSupplier.getCode());
    existing.setName(updatedSupplier.getName());
    existing.setContactName(updatedSupplier.getContactName());
    existing.setEmail(updatedSupplier.getEmail());
    existing.setPhone(updatedSupplier.getPhone());
    existing.setAddress(updatedSupplier.getAddress());
    existing.setActive(updatedSupplier.isActive());

    return supplierRepository.save(existing);
}
    public void delete(Long id) {
        supplierRepository.deleteById(id);
    }
}
