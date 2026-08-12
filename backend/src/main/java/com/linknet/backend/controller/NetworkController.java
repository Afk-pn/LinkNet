package com.linknet.backend.controller;

import com.linknet.backend.service.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/network")
public class NetworkController {

    @Autowired
    private Graph graphService;

    //Web API URL to fetch computed mutual friend recommendations via BFS 
    //URL pattern will be: /api/network/1recommendations/
    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<Integer>> getSuggestions(@PathVariable Integer userId) {
        List<Integer> recommendations = graphService.getFriendRecommendations(userId);
        
        if (recommendations.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return a clean 204 if empty
        }
        
        return ResponseEntity.ok(recommendations);
    }
}
