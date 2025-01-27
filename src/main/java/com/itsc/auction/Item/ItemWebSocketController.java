package com.itsc.auction.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ItemWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyItemChange(String eventType, Item item) {
        messagingTemplate.convertAndSend("/topic/items", new ItemEvent(eventType, item));
    }

    public static class ItemEvent {
        private String eventType;
        private Item item;

        public ItemEvent(String eventType, Item item) {
            this.eventType = eventType;
            this.item = item;
        }

        public String getEventType() {
            return eventType;
        }

        public Item getItem() {
            return item;
        }
    }
}
