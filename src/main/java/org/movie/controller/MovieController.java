package org.movie.controller;

import liquibase.license.LicenseService;
import org.movie.dao.MovieDAO;
import org.movie.model.Actors;
import org.movie.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class MovieController {

    @Autowired
    private MovieDAO movieDAO;

    @GetMapping
    public String showAllMovies(Model model) {
        List<Movie> movies = movieDAO.allMovies();
        model.addAttribute("movies", movies);

        return "index";
    }

    @GetMapping("/movie/{id}")
    public String getMovie(@PathVariable int id, Model model) {

        Movie movie = movieDAO.getMovieById(id);

        List<Actors> actors = movieDAO.actorsByMovie(id);

        System.out.println(actors);

        model.addAttribute("movie", movie);
        model.addAttribute("actors", actors);

        return "movie";
    }


    @GetMapping("/save-movie")
    public String saveMovie(Model model) {
        Movie movie = new Movie();
        model.addAttribute("movie", movie);
        return "save-movie";
    }


    @PostMapping( "/save-movie")
    public String submitMovie(@ModelAttribute Movie movie, Model model) {
        movieDAO.saveMovie(movie);
        return "index";
    }







}
