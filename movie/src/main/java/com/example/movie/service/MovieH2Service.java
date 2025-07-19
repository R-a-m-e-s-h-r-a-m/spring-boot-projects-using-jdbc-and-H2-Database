package com.example.movie.service;

import com.example.movie.model.Movie;
import com.example.movie.model.MovieRowMapper;
import com.example.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieH2Service implements MovieRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Movie> getMovies() {
        String sql = "SELECT * FROM MOVIELIST ORDER BY movieId";
        List<Movie> movies = db.query(sql, new MovieRowMapper());
        return new ArrayList<>(movies);
    }

    @Override
    public Movie getMovieById(int movieId) {
        String sql = "SELECT * FROM MOVIELIST WHERE movieId = ?";
        try {
            return db.queryForObject(sql, new MovieRowMapper(), movieId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
    }

    @Override
    public Movie addMovie(Movie movie) {
        String sql = "INSERT INTO MOVIELIST(movieName, leadActor) VALUES(?, ?)";
        db.update(sql, movie.getMovieName(), movie.getLeadActor());
        // Retrieve the inserted movie using its unique combination of movieName and
        // leadActor.
        Movie savedMovie = db.queryForObject(
                "SELECT * FROM MOVIELIST WHERE movieName = ? AND leadActor = ?",
                new MovieRowMapper(),
                movie.getMovieName(),
                movie.getLeadActor());
        return savedMovie;
    }

    @Override
    public Movie updateMovie(int movieId, Movie movie) {
        String sql = "UPDATE MOVIELIST SET movieName = ?, leadActor = ? WHERE movieId = ?";
        int updated = db.update(sql, movie.getMovieName(), movie.getLeadActor(), movieId);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
        return getMovieById(movieId);
    }

    @Override
    public void deleteMovie(int movieId) {
        // Execute the delete query regardless of whether the movie exists.
        db.update("DELETE FROM MOVIELIST WHERE movieId = ?", movieId);
        // No exception is thrown if no record was deleted; the controller will return
        // OK status.
    }

}
