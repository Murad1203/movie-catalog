package org.movie.controller;

import org.movie.dao.MovieDAO;
import org.movie.model.Actors;
import org.movie.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return "redirect:/";
    }


    @GetMapping("/update-movie/{id}")
    public String updateMovie(@PathVariable int id, Model model) {

        Movie movie = movieDAO.getMovieById(id);
        List<Actors> actors = movieDAO.actorsByMovie(id);

        List<String> actorConcat =  actors.stream()
                        .map(actors1 -> {
                            String fio = actors1.getName() +  " " + actors1.getLastname();
                            return fio;
                        })
                        .collect(Collectors.toList());

        movie.setActors(actorConcat);

//        System.out.println(actors.get(0);

        model.addAttribute("movie", movie);
        model.addAttribute("actor", movieDAO.getIdsActorsByMovie(id));
        return "update";

    }

    @PostMapping("/update-movie/{id}")
    public String submitUpdate(@PathVariable int id, Movie movie) {

        movieDAO.updateMovie(id, movie);
        return "redirect:/movie/" + id;
    }

    @RequestMapping("/delete/{id}")
    public String deleteMovie(@PathVariable int id) {
        movieDAO.deleteMovie(id);
        return "redirect:/";
    }

}
