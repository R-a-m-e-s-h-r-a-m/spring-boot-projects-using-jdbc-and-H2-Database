package com.example.movie.controller;

import com.example.movie.model.Movie;
import com.example.movie.service.MovieH2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
public class MovieController {

    @Autowired
    private MovieH2Service service;

    // API 1: GET /movies - return list of all movies
    @GetMapping("/movies")
    public ArrayList<Movie> getMovies() {
        return service.getMovies();
    }

    // API 2: POST /movies - create a new movie (movieId auto-incremented)
    @PostMapping("/movies")
    public Movie addMovie(@RequestBody Movie movie) {
        return service.addMovie(movie);
    }

    // API 3: GET /movies/{movieId} - return movie details or NOT_FOUND if absent
    @GetMapping("/movies/{movieId}")
    public Movie getMovieById(@PathVariable int movieId) {
        return service.getMovieById(movieId);
    }

    // API 4: PUT /movies/{movieId} - update movie details or NOT_FOUND if absent
    @PutMapping("/movies/{movieId}")
    public Movie updateMovie(@PathVariable int movieId, @RequestBody Movie movie) {
        return service.updateMovie(movieId, movie);
    }

    // API 5: DELETE /movies/{movieId} - delete movie or return NOT_FOUND if absent
    @DeleteMapping("/movies/{movieId}")
    public void deleteMovie(@PathVariable int movieId) {
        service.deleteMovie(movieId);
    }
}
