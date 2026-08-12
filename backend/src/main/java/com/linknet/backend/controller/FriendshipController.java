package com.linknet.backend.controller;

import com.linknet.backend.entity.Friendship;
import com.linknet.backend.service.FriendshipService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping
    public Friendship createFriendship(@RequestParam Integer userId1,
                                        @RequestParam Integer userId2) {
        return friendshipService.createFriendship(userId1, userId2);
    }

    @GetMapping
    public List<Friendship> getAllFriendships() {
        return friendshipService.getAllFriendships();
    }

    @GetMapping("/{id}")
    public Friendship getFriendshipById(@PathVariable Integer id) {
        return friendshipService.findFriendshipById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFriendship(@PathVariable Integer id) {
        friendshipService.deleteFriendship(id);
    }

    @DeleteMapping
    public void deleteFriendshipBetween(@RequestParam Integer userId1,
                                         @RequestParam Integer userId2) {
        friendshipService.deleteFriendshipBetween(userId1, userId2);
    }
}