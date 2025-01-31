package com.itsc.auction.Bid;

import com.itsc.auction.Auction.Auction;
import com.itsc.auction.Auction.AuctionService;
import com.itsc.auction.Auction.dto.UpdateAuctionDto;
import com.itsc.auction.Bid.dto.PlaceBidDto;
import com.itsc.auction.Item.Item;
import com.itsc.auction.User.User;
import com.itsc.auction.User.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Bid> placeBid(@RequestBody PlaceBidDto placeBidDto, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("user");
        String username = claims.getSubject();

        User user = userService.findUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Long auctionId = placeBidDto.getAuctionId();
        BigDecimal bidAmount = placeBidDto.getBidAmount();

        Auction auction = auctionService.getAuctionById(auctionId).orElseThrow();
        Item item = auction.getItem();
        User owner = item.getOwner();

        // the owner of the item cannot place a bid
        if (owner.getUsername().equals(username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // if (item.getStatus() != "ACTIVE") {
        //     return ResponseEntity.badRequest().build();
        // }

        if (bidAmount.compareTo(auction.getCurrentPrice()) <= 0) {
            return ResponseEntity.badRequest().build();
        }

        // update the current price of the auction
        UpdateAuctionDto updateAuctionDto = new UpdateAuctionDto();
        updateAuctionDto.setCurrentPrice(bidAmount);
        updateAuctionDto.setHighestBidder(user);
        auctionService.updateAuction(auctionId, updateAuctionDto);

        Bid bid = bidService.placeBid(auction, user, bidAmount);
        return ResponseEntity.ok(bid);
    }

    @GetMapping
    public ResponseEntity<List<Bid>> getMyBids(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("user");
        String username = claims.getSubject();

        User user = userService.findUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Bid> bids = bidService.getMyBids(user.getId());
        return ResponseEntity.ok(bids);
    }


}