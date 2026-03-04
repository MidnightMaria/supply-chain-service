package com.agnesmaria.supplychain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Purchase Order entity representing an order to suppliers")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the purchase order", example = "1")
    private Long id;

    @Column(unique = true)
    @Schema(
        description = "Unique order number for tracking", 
        example = "PO-2024-001",
        required = true
    )
    private String orderNumber;

    @ManyToOne
    @Schema(description = "Supplier associated with this purchase order")
    private Supplier supplier;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
        description = "Date when the order was created", 
        example = "2024-01-15",
        type = "string", 
        format = "date"
    )
    private LocalDate orderDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
        description = "Expected delivery date for the order", 
        example = "2024-02-01",
        type = "string", 
        format = "date"
    )
    private LocalDate expectedDeliveryDate;

    @Schema(
        description = "Current status of the purchase order",
        example = "PENDING",
        allowableValues = {"DRAFT", "PENDING", "APPROVED", "RECEIVED", "COMPLETED"},
        required = true
    )
    private String status;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Schema(description = "List of items included in this purchase order")
    private List<PurchaseOrderItem> items;
}