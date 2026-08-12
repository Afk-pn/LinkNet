package com.linknet.backend.service;

import com.linknet.backend.entity.Friendship;
import com.linknet.backend.repository.FriendshipRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class FriendshipService {

    @Autowired
    private FriendshipRep friendshipRepository;

    public Friendship createFriendship(Integer userId1, Integer userId2) {

        if (userId1.equals(userId2)) {
            throw new IllegalStateException("Cannot friend yourself");
        }

        boolean alreadyExists = friendshipRepository.findAll().stream()
            .anyMatch(f ->
                (f.getUserId1().equals(userId1) && f.getUserId2().equals(userId2)) ||
                (f.getUserId1().equals(userId2) && f.getUserId2().equals(userId1))
            );

        if (alreadyExists) {
            throw new IllegalStateException("Friendship already exists");
        }

        return friendshipRepository.save(new Friendship(userId1, userId2));
    }

    public List<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

    public Friendship findFriendshipById(Integer friendshipId) {
        return friendshipRepository.findById(friendshipId)
            .orElseThrow(() -> new IllegalStateException("Friendship not found with id: " + friendshipId));
    }

    public void deleteFriendship(Integer friendshipId) {
        Friendship friendship = findFriendshipById(friendshipId);
        friendshipRepository.delete(friendship);
    }

    public void deleteFriendshipBetween(Integer userId1, Integer userId2) {
        Friendship friendship = friendshipRepository.findAll().stream()
            .filter(f ->
                (f.getUserId1().equals(userId1) && f.getUserId2().equals(userId2)) ||
                (f.getUserId1().equals(userId2) && f.getUserId2().equals(userId1))
            )
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Friendship does not exist between these users"));

        friendshipRepository.delete(friendship);
    }
}