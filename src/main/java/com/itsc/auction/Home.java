package com.itsc.auction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {
    @GetMapping("/")
    String getHome(){
        return "Welcomse";
    }
}
