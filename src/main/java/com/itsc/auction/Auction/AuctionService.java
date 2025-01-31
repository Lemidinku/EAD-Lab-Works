package com.itsc.auction.Auction;

import com.itsc.auction.Auction.dto.UpdateAuctionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private AuctionWebSocketController auctionWebSocketController;

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    public List<Auction> getMyAuctions(String username) {
        List<Auction> auctions = auctionRepository.findByItemOwnerUsername(username);
        return auctions;


    }

    public Auction createAuction(Auction auction) {
        auction.setStatus("ONGOING");
        Auction returnedAuction = auctionRepository.save(auction);
        return returnedAuction;
    }

    public Auction updateAuction(Long id, UpdateAuctionDto auctionDetails) {
        Auction auction = auctionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Auction not found with id: " + id));

        if (auctionDetails.getStartTime() != null) {
            auction.setStartTime(auctionDetails.getStartTime());
        }
        if (auctionDetails.getEndTime() != null) {
            auction.setEndTime(auctionDetails.getEndTime());
        }
        if (auctionDetails.getStatus() != null) {
            auction.setStatus(auctionDetails.getStatus());
        }
        if (auctionDetails.getHighestBidder() != null) {
            auction.setHighestBidder(auctionDetails.getHighestBidder());
        }
        if (auctionDetails.getCurrentPrice() != null) {
            auction.setCurrentPrice(auctionDetails.getCurrentPrice());
        }

        Auction updatedAuction = auctionRepository.save(auction);

        auctionWebSocketController.notifyAuctionChange("UPDATE", updatedAuction);

        return updatedAuction;
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
