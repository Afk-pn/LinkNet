package com.linknet.backend.service;

import com.linknet.backend.entity.Friendship;
import com.linknet.backend.repository.FriendshipRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Graph {

    private final FriendshipRep friendshipRepository;

    public Graph(FriendshipRep friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public List<Integer> getFriendRecommendations(Integer targetUserId) {
        // Step 1: Grab all friendship pairs from MySQL
        List<Friendship> totalConnections = friendshipRepository.findAll();

        // Step 2: Build the map of who knows who
        Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
        for (Friendship edge : totalConnections) {
            Integer u1 = edge.getUserId1();
            Integer u2 = edge.getUserId2();
            
            adjacencyList.computeIfAbsent(u1, k -> new ArrayList<>()).add(u2);
            adjacencyList.computeIfAbsent(u2, k -> new ArrayList<>()).add(u1);
        }

        //  if the user isn't in our map, stop immediately
        if (!adjacencyList.containsKey(targetUserId)) {
            return new ArrayList<>();
        }

        // Step 3: Set up tracking structures for our BFS search
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        
        List<Integer> friendsList= adjacencyList.get(targetUserId);
        Set<Integer> immediateFriends= new HashSet<>();
        for(Integer en: friendsList){
            immediateFriends.add(en);
        }

        queue.add(targetUserId);
        visited.add(targetUserId);

        Map<Integer, Integer> separationLevel = new HashMap<>();
        separationLevel.put(targetUserId, 0);

        List<Integer> recommendedIds = new ArrayList<>();

        // Step 4: Run the BFS loop to look for friends-of-friends
        while (!queue.isEmpty()) {
            Integer currentVertex = queue.poll();
            int currentDepth = separationLevel.get(currentVertex);

            if (currentDepth >= 2) {
                continue; 
            }

            List<Integer> neighbors = adjacencyList.getOrDefault(currentVertex, new ArrayList<>());
            for (Integer peer : neighbors) {
                if (!visited.contains(peer)) {
                    visited.add(peer);
                    int nextDepth = currentDepth + 1;
                    separationLevel.put(peer, nextDepth);
                    queue.add(peer);

                
                    if (nextDepth == 2 && !immediateFriends.contains(peer) && !peer.equals(targetUserId)) {
                        recommendedIds.add(peer);
                    }
                }
            }
        }

        return recommendedIds;
    }
}
