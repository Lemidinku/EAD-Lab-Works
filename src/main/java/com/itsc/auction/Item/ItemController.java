package com.itsc.auction.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.itsc.auction.Item.dto.CreateItemDto;
import com.itsc.auction.User.User;
import com.itsc.auction.User.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<Item> getMyItems(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("user");
        String username = claims.getSubject();
        return itemService.getMyItems(username); // currently this only returns items for a specific user
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<Item> getItemById(@PathVariable Long id) {
    //     Item item = itemService.getItemById(id);
    //     return ResponseEntity.ok(item);
    // }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody CreateItemDto createItemDto, HttpServletRequest request) {
        System.out.println(createItemDto);
        Item item = new Item();

        item.setName(createItemDto.getName());
        item.setDescription(createItemDto.getDescription());
        item.setStatus(createItemDto.getStatus());
        item.setStartingPrice(createItemDto.getStartingPrice()); 

        Claims claims = (Claims) request.getAttribute("user");
        String username = claims.getSubject();

        User owner = userService.findUserByUsername(username);
        item.setOwner(owner);

        Item createdItem = itemService.createItem(item);
        return ResponseEntity.ok(createdItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable Long id, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("user");
        String username = claims.getSubject();

        Item item = itemService.getItemById(id);
        User owner = item.getOwner();

        Map<String, Object> response = new HashMap<>();
        if (!owner.getUsername().equals(username)){
            response.put("error", "User not authenticated.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
