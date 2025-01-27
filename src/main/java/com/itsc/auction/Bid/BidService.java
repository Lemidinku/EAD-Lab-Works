package com.itsc.auction.Bid;

import com.itsc.auction.Auction.Auction;
import com.itsc.auction.User.User;
import com.itsc.auction.exception.InvalidBidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    public Bid placeBid(Auction auction, User user, BigDecimal bidAmount) {
        
        if (auction.getCurrentPrice().compareTo(bidAmount) >= 0) {
            throw new InvalidBidException("Bid amount must be greater than the current price");
        }
        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setUser(user);
        bid.setBidAmount(bidAmount);
        return bidRepository.save(bid);
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

}