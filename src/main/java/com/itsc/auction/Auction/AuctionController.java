package com.itsc.auction.Auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.itsc.auction.Auction.dto.CreateAuctionDto;
import com.itsc.auction.Auction.dto.UpdateAuctionDto;
import com.itsc.auction.Item.Item;
import com.itsc.auction.Item.ItemService;
import com.itsc.auction.User.User;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable Long id) {
        Optional<Auction> auction = auctionService.getAuctionById(id);
        return auction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Auction> createAuction(@RequestBody CreateAuctionDto createAuctionDto, HttpServletRequest request) {
        
        Claims claims = (Claims) request.getAttribute("user");
        String username = claims.getSubject();
        Auction auction = new Auction();

        Item item = itemService.getItemById(createAuctionDto.getItemId());
        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        User owner = item.getOwner();

        if (!owner.getUsername().equals(username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        auction.setItem(item);
        auction.setStartTime(createAuctionDto.getStartTime());
        auction.setEndTime(createAuctionDto.getEndTime());
        auction.setStatus(createAuctionDto.getStatus());
        auction.setCurrentPrice(item.getStartingPrice());

        Auction createdAuction = auctionService.createAuction(auction);
        return ResponseEntity.ok(createdAuction);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Auction> updateAuction(@PathVariable Long id, @RequestBody UpdateAuctionDto updateAuctionDto, HttpServletRequest request) {

        Claims claims = (Claims) request.getAttribute("user");
        String username = claims.getSubject();
        Auction auction = auctionService.getAuctionById(id).orElse(null);

        if (auction == null) {
            return ResponseEntity.notFound().build();
        }

        Item item = auction.getItem();
        User owner = item.getOwner();

        if (!owner.getUsername().equals(username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {

            Auction updatedAuction = auctionService.updateAuction(id, updateAuctionDto);
            return ResponseEntity.ok(updatedAuction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id , HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("user");
        String username = claims.getSubject();
        Auction auction = auctionService.getAuctionById(id).orElse(null);

        if (auction == null) {
            return ResponseEntity.notFound().build();
        }

        Item item = auction.getItem();
        User owner = item.getOwner();

        if (!owner.getUsername().equals(username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        auctionService.deleteAuction(id);
        return ResponseEntity.noContent().build();
    }
}
