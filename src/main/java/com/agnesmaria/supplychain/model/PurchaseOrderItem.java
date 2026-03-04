package com.agnesmaria.supplychain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Purchase Order Item entity representing individual products in a purchase order")
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the purchase order item", example = "1")
    private Long id;

    @Schema(
        description = "Stock Keeping Unit (SKU) of the product",
        example = "PROD-001-XL",
        required = true
    )
    private String productSku;

    @Schema(
        description = "Quantity of the product ordered",
        example = "50",
        minimum = "1",
        required = true
    )
    private int quantity;

    @Schema(
        description = "Unit price of the product",
        example = "25.50",
        minimum = "0.0",
        required = true
    )
    private double unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    @ToString.Exclude
    @JsonBackReference
    @Schema(
        description = "Parent purchase order that this item belongs to",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private PurchaseOrder purchaseOrder;
}