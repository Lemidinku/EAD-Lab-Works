package com.itsc.auction;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages")
public class PagesController {

    @GetMapping("/login")
    public String viewLoginPage() {
        return "signin";
    }

    @GetMapping("/register")
    public String viewRegisterPage() {
        return "signup";
    }

    
    @GetMapping("/home")
    public String viewHomePage() {
        return "home";
    }
    @GetMapping("/auction")
    public String viewAuctionPage() { // to show auction details
        return "auction";
    }
}

