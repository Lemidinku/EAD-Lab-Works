package com.itsc.auction.Auction;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuctionPageController {

    @GetMapping("/auctions-page")
    public String viewAuctionsPage() {
        return "auctions"; // This corresponds to items.html in the templates directory
    }
}

