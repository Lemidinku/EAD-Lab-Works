package com.itsc.auction.Item;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemPageController {

    @GetMapping("/items-page")
    public String viewItemsPage() {
        return "items"; // This corresponds to items.html in the templates directory
    }
}
