package com.itsc.auction.Auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.itsc.auction.Item.Item;
import com.itsc.auction.User.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Item is required for the auction")
    @OneToOne() 
    @JoinColumn(name = "item_id", nullable = false, unique = true)
    private Item item;

    @NotNull(message = "Start time is required")
    @Column(nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Column(nullable = false)
    private LocalDateTime endTime;

    @NotNull(message = "Current price is required")
    @Column(nullable = false)
    private BigDecimal currentPrice;

    @ManyToOne()
    @JoinColumn(name = "highest_bidder_id")
    private User highestBidder;

    @NotNull(message = "Status is required")
    @Column(nullable = false)
    private String status; // Example values: ONGOING, FINISHED, CANCELLED

    // Validate that startTime is earlier than endTime.
    @AssertTrue(message = "Start time must be earlier than end time")
    public boolean isStartTimeBeforeEndTime() {
        return startTime.isBefore(endTime);
    }
}