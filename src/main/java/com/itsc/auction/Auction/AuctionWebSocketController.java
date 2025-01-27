package com.itsc.auction.Auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class AuctionWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyAuctionChange(String eventType, Auction auction) {
        messagingTemplate.convertAndSend("/topic/auctions", new AuctionEvent(eventType, auction));
    }

    public static class AuctionEvent {
        private String eventType;
        private Auction auction;

        public AuctionEvent(String eventType, Auction auction) {
            this.eventType = eventType;
            this.auction = auction;
        }

        public String getEventType() {
            return eventType;
        }

        public Auction getAuction() {
            return auction;
        }
    }
}
