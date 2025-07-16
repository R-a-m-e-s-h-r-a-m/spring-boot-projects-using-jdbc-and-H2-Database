package com.example.player.service;

import com.example.player.model.Player;
import com.example.player.model.PlayerRowMapper;
import com.example.player.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerH2Service implements PlayerRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Player> getPlayers() {
        String sql = "SELECT * FROM TEAM ORDER BY playerId";
        List<Player> players = db.query(sql, new PlayerRowMapper());
        return new ArrayList<>(players);
    }

    @Override
    public Player getPlayerById(int playerId) {
        String sql = "SELECT * FROM TEAM WHERE playerId = ?";
        try {
            return db.queryForObject(sql, new PlayerRowMapper(), playerId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
    }

    @Override
    public Player addPlayer(Player player) {
        String sql = "INSERT INTO TEAM(playerName, jerseyNumber, role) VALUES(?, ?, ?)";
        db.update(sql, player.getPlayerName(), player.getJerseyNumber(), player.getRole());
        // Retrieve the inserted player using its unique combination of attributes.
        Player savedPlayer = db.queryForObject(
                "SELECT * FROM TEAM WHERE playerName = ? AND jerseyNumber = ? AND role = ?",
                new PlayerRowMapper(),
                player.getPlayerName(), player.getJerseyNumber(), player.getRole());
        return savedPlayer;
    }

    @Override
    public Player updatePlayer(int playerId, Player player) {
        String sql = "UPDATE TEAM SET playerName = ?, jerseyNumber = ?, role = ? WHERE playerId = ?";
        int updated = db.update(sql, player.getPlayerName(), player.getJerseyNumber(), player.getRole(), playerId);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
        return getPlayerById(playerId);
    }

    @Override
    public void deletePlayer(int playerId) {
        // Execute the delete; even if no row is deleted, we return OK status.
        db.update("DELETE FROM TEAM WHERE playerId = ?", playerId);
    }
}
