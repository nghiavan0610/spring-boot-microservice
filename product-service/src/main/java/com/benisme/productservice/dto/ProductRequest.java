package com.benisme.productservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductRequest {

    @NotNull(message = "username shouldn't be null")
    @NotEmpty
    private String name;

    private String description;
    private BigDecimal price;
}
