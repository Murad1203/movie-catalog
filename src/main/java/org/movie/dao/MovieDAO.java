package org.movie.dao;

import org.movie.dto.ActorsId;
import org.movie.model.Actors;
import org.movie.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class MovieDAO {


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MovieDAO(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public List<Movie> allMovies() {
        return jdbcTemplate.query("select * from movies", BeanPropertyRowMapper.newInstance(Movie.class));
    }


    public Actors getActor(int id) {
        return (Actors) jdbcTemplate.query("select * from actors WHERE id=?", BeanPropertyRowMapper.newInstance(Actors.class), id)
                .stream().findAny().orElse(null);
    }


    public Movie getMovieById(int id) {
        return jdbcTemplate.query("select * from movies WHERE id = ?", BeanPropertyRowMapper.newInstance(Movie.class), id)
                .stream().findAny().orElse(null);
    }

    public List<Actors> actorsByMovie(int movieId) {
        List<ActorsId> actors_id = jdbcTemplate.query("select actor_id from movies_actors WHERE movie_id=?", BeanPropertyRowMapper.newInstance(ActorsId.class), movieId);

        List<Actors> actors = new ArrayList<>();

        actors_id.forEach(actor -> {
            actors.add(getActor(actor.getActorId()));
        });
        return actors;
    }



    public void saveMovie(Movie movie) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
         jdbcTemplate.update("INSERT INTO movies VALUES(9, ?, ?, ?, ?, ?, ?)",
//                keyHolder,
                movie.getName(),
                movie.getDate(),
                movie.getDescription(),
                movie.getGenre(),
                movie.getActors(),
                movie.getRaiting());

        List<String> actors = movie.getActors();
        System.out.println("====================" + actors);
        List<Actors> actorByMovie = new ArrayList<>();


        actors.forEach(actor -> {

            String name = actor.split(" ")[0];
            String surname = actor.split(" ")[1];

            Actors actor1 = new Actors();
            actor1.setName(name);
            actor1.setLastname(surname);

            actorByMovie.add(actor1);

        });

        System.out.println("========================= " + actorByMovie);
        //Метод для добавления данных
        saveActor(actorByMovie);

        //Добавляет id фильма и актера в таблицу
        actorByMovie.forEach(actor ->  {
            if (actor != null) {
                jdbcTemplate.update("INSERT INTO movies_actors VALUES (?, ?)", movie.getId(), actor.getId());
            }
        });


     }





     public void saveActor(List<Actors> actors) {

            actors.forEach(actor -> {
                if (actor != null) {
                    jdbcTemplate.update("INSERT INTO actors VALUES(?, ?)", actor.getName(), actor.getLastname());
                }
         });
     }

    public void updateMovie(int id, Movie updateMovie) {
        jdbcTemplate.update("UPDATE movies SET name=?, date=?, description=?, genre=?, actors=?, raiting=? WHERE id=?",
                updateMovie.getName(),
                updateMovie.getDate(),
                updateMovie.getDescription(),
                updateMovie.getGenre(),
                updateMovie.getActors(),
                updateMovie.getRaiting(), id);
    }

    public void deleteMovie(int id) {
        jdbcTemplate.update("DELETE FROM movies WHERE id=?", id);
    }



}
