package com.agnesmaria.supplychain.service;

import com.agnesmaria.supplychain.model.PurchaseOrder;
import com.agnesmaria.supplychain.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {
    private final PurchaseOrderRepository poRepository;

    public List<PurchaseOrder> getAll() {
        return poRepository.findAll();
    }

    public PurchaseOrder create(PurchaseOrder po) {
        return poRepository.save(po);
    }
}
