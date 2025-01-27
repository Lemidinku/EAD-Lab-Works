package com.itsc.auction.Auction;

import com.itsc.auction.Auction.dto.UpdateAuctionDto;
import com.itsc.auction.Item.Item;
import com.itsc.auction.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    public Auction createAuction(Auction auction) {
        // Additional validations (e.g., check if item is already in an auction)
        auction.setStatus("ONGOING");
        Auction returnedAuction = auctionRepository.save(auction);
        System.out.println("Auction created: " + returnedAuction);
        return returnedAuction;
    }

    public Auction updateAuction(Long id, UpdateAuctionDto auctionDetails) {
        return auctionRepository.findById(id).map(auction -> {
            if (auctionDetails.getStartTime() != null) {
                auction.setStartTime(auctionDetails.getStartTime());
            }
            if (auctionDetails.getEndTime() != null) {
                auction.setEndTime(auctionDetails.getEndTime());
            }
            if (auctionDetails.getStatus() != null) {
                auction.setStatus(auctionDetails.getStatus());
            }
            return auctionRepository.save(auction);
        }).orElseThrow(() -> new RuntimeException("Auction not found with id: " + id));
    }

    public void deleteAuction(Long id) {
        auctionRepository.deleteById(id);
    }

    public List<Auction> getAuctionsByStatus(String status) {
        return auctionRepository.findByStatus(status);
    }

    public void closeExpiredAuctions() {
        List<Auction> expiredAuctions = auctionRepository.findByEndTimeBeforeAndStatus(LocalDateTime.now(), "ONGOING");
        for (Auction auction : expiredAuctions) {
            auction.setStatus("FINISHED");
            auctionRepository.save(auction);
        }
    }
}
