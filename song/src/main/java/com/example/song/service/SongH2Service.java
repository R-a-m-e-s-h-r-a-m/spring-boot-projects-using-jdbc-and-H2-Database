/*
 * 
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here

package com.example.song.service;

import com.example.song.model.Song;
import com.example.song.model.SongRowMapper;
import com.example.song.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongH2Service implements SongRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Song> getSongs() {
        List<Song> songList = db.query("select * from PLAYLIST order by songId", new SongRowMapper());
        return new ArrayList<>(songList);
    }

    @Override
    public Song getSongById(int songId) {
        try {
            Song song = db.queryForObject("select * from PLAYLIST where songId = ?", new SongRowMapper(), songId);
            return song;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Song addSong(Song song) {
        db.update("insert into PLAYLIST(songName, lyricist, singer, musicDirector) values(?,?,?,?)",
                song.getSongName(),
                song.getLyricist(),
                song.getSinger(),
                song.getMusicDirector());
        // Retrieve the inserted song using its unique combination of details.
        Song savedSong = db.queryForObject(
                "select * from PLAYLIST where songName = ? and lyricist = ? and singer = ? and musicDirector = ?",
                new SongRowMapper(),
                song.getSongName(),
                song.getLyricist(),
                song.getSinger(),
                song.getMusicDirector());
        return savedSong;
    }

    @Override
    public Song updateSong(int songId, Song song) {
        int updated = db.update(
                "update PLAYLIST set songName = ?, lyricist = ?, singer = ?, musicDirector = ? where songId = ?",
                song.getSongName(),
                song.getLyricist(),
                song.getSinger(),
                song.getMusicDirector(),
                songId);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return getSongById(songId);
    }

    @Override
    public void deleteSong(int songId) {
        int deleted = db.update("delete from PLAYLIST where songId = ?", songId);
        
    }
}
