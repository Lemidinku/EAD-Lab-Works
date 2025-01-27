package com.itsc.auction.Auction.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAuctionDto {

    private LocalDateTime startTime; // Optional: New start time

    private LocalDateTime endTime; // Optional: New end time

    @Positive(message = "Current price must be a positive value")
    private BigDecimal currentPrice; // Optional: New current price

    private String status;
}

