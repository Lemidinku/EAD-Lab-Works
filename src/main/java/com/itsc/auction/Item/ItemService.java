package com.itsc.auction.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itsc.auction.User.User;
import com.itsc.auction.User.UserService;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemWebSocketController itemWebSocketController;

    public List<Item> getMyItems(String userName) {

        User owner = userService.findUserByUsername(userName);
        Long ownerId = owner.getId();
        return itemRepository.findByOwnerId(ownerId);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id " + id));
    }

    public Item createItem(Item item) {
        System.out.println("createItem" + item);
        Item createdItem = itemRepository.save(item);
        itemWebSocketController.notifyItemChange("CREATE", createdItem); // Notify WebSocket clients
        return createdItem;
    }

    public void deleteItem(Long id) {

        Item item = getItemById(id);
        if (item == null) {
            throw new RuntimeException("Item not found with id " + id);
        }
        itemRepository.delete(item);
        itemWebSocketController.notifyItemChange("DELETE", item); // Notify WebSocket clients
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
