package com.itsc.auction.Bid.dto;

import java.math.BigDecimal;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceBidDto {
    
    @NotNull(message = "Auction ID is required")
    private Long auctionId;

    @NotNull(message = "Bid amount is required")
    private BigDecimal bidAmount;


}
