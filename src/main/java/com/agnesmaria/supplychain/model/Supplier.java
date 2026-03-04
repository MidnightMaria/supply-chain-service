package com.agnesmaria.supplychain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Supplier entity representing a vendor or business partner")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the supplier", example = "1")
    private Long id;

    @Column(unique = true)
    @Schema(
        description = "Unique supplier code for identification",
        example = "SUP-001",
        required = true
    )
    private String code;

    @Schema(
        description = "Full name of the supplier company",
        example = "PT. Example Supplier Indonesia",
        required = true
    )
    private String name;

    @Schema(
        description = "Contact person name for communication",
        example = "Budi Santoso"
    )
    private String contactName;

    @Schema(
        description = "Email address for official communication",
        example = "contact@examplesupplier.com"
    )
    private String email;

    @Schema(
        description = "Phone number for contact",
        example = "+62-21-1234567"
    )
    private String phone;

    @Schema(
        description = "Complete address of the supplier",
        example = "Jl. Sudirman No. 123, Jakarta Selatan, DKI Jakarta 12190"
    )
    private String address;

    @Schema(
        description = "Status indicating whether the supplier is active",
        example = "true",
        defaultValue = "true"
    )
    private boolean active = true;
}