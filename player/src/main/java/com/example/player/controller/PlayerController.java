package com.example.player.controller;

import com.example.player.model.Player;
import com.example.player.service.PlayerH2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
public class PlayerController {

    @Autowired
    private PlayerH2Service service;

    // API 1: GET /players - return list of all players
    @GetMapping("/players")
    public ArrayList<Player> getPlayers() {
        return service.getPlayers();
    }

    // API 2: POST /players - add a new player (playerId auto-incremented)
    @PostMapping("/players")
    public Player addPlayer(@RequestBody Player player) {
        return service.addPlayer(player);
    }

    // API 3: GET /players/{playerId} - return player details or NOT_FOUND if absent
    @GetMapping("/players/{playerId}")
    public Player getPlayerById(@PathVariable int playerId) {
        return service.getPlayerById(playerId);
    }

    // API 4: PUT /players/{playerId} - update player details or NOT_FOUND if absent
    @PutMapping("/players/{playerId}")
    public Player updatePlayer(@PathVariable int playerId, @RequestBody Player player) {
        return service.updatePlayer(playerId, player);
    }

    // API 5: DELETE /players/{playerId} - delete player; returns OK even if
    // playerId not found
    @DeleteMapping("/players/{playerId}")
    public void deletePlayer(@PathVariable int playerId) {
        service.deletePlayer(playerId);
    }
}
