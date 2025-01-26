package com.itsc.auction.Item.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateItemDto {

    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    @NotNull(message = "Starting price is required")
    private BigDecimal startingPrice;

    // @NotNull(message = "Owner ID is required")
    // private Long ownerId;

    @NotBlank(message = "Status is required")
    private String status;
}
