package org.movie.dao;

import org.movie.model.Actor;
import org.movie.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Repository
public class FilterDao {

    @Autowired
    private MovieDAO movieDAO;

    public List<Movie> filter(String nameFilm, String genre, String lastnameActor) {
        List<Movie> movies = movieDAO.allMovies();
        List<Actor> actors = movieDAO.allActors();

        System.out.println("movies " + movies);
        System.out.println("name " + nameFilm +  " genre " + genre  + " lastname " + lastnameActor);


        boolean isName = nameFilm != "null";

        if (!genre.equals("null")) {

            return movies.stream()
                    .filter((movie) -> movie.getGenre().equals(genre))
                    .collect(Collectors.toList());
        }
        else if (!nameFilm.equals("null")) {

            return movies.stream()
                    .filter((movie) -> movie.getName().equals(nameFilm))
                    .collect(Collectors.toList());
        }
        else if (!Objects.equals(lastnameActor, "null")) {

            List<Actor> act = actors.stream()
                    .filter((actor) -> actor.getLastname().equals(lastnameActor))
                    .collect(Collectors.toList());
            for (Actor act1: act) {
                List<Movie> movies1 = movieDAO.getMovieByActorsId(act1.getId());
                System.out.println("-----" + movies1);
                return movies1;
            }
        }
        else  {
            System.out.println("ddd");
        }
        return movies;
    }


}
