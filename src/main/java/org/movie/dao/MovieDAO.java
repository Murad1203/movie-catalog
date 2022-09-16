package org.movie.dao;

import org.movie.dao.other.MapActor;
import org.movie.dto.ActorsId;
import org.movie.model.Actor;
import org.movie.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.*;

@Repository
public class MovieDAO {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private GeneratedKeyHolder keyHolder;

    @Autowired
    private MapActor mapActor;

    @Autowired
    public MovieDAO(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    public List<Movie> allMovies() {
        return jdbcTemplate.query("select * from movies ORDER BY id DESC",
                BeanPropertyRowMapper.newInstance(Movie.class));
    }
    public Actor getActor(int id) {
        return (Actor) jdbcTemplate.query("select * from actors WHERE id=?",
                        BeanPropertyRowMapper.newInstance(Actor.class), id)
                .stream().findAny().orElse(null);
    }
    public Movie getMovieById(int id) {
        return jdbcTemplate.query("select * from movies WHERE id = ?",
                        BeanPropertyRowMapper.newInstance(Movie.class), id)
                .stream().findAny().orElse(null);
    }
    public List<Actor> actorsByMovie(int movieId) {
        List<ActorsId> actors_id = jdbcTemplate.query("select actor_id from movies_actors WHERE movie_id=?",
                BeanPropertyRowMapper.newInstance(ActorsId.class), movieId);
        List<Actor> actors = new ArrayList<>();

        actors_id.forEach(actor -> {
            actors.add(getActor(actor.getActorId()));
        });
        return actors;
    }

    public List<Movie> getMovieByActorsId(int id) {
        System.out.println("id = " + id);
        List<ActorsId> moviesId = jdbcTemplate.query("select movie_id from movies_actors WHERE actor_id=?",
                BeanPropertyRowMapper.newInstance(ActorsId.class), id);
        System.out.println("moviesId" + moviesId);
        List<Movie> movies = new ArrayList<>();

        moviesId.stream()
                .map(movieId -> {
                    Movie movie = getMovieById(movieId.getActorId());
                    movies.add(movie);
                    return movies;
                });

        return movies;
    }

    public List<ActorsId> getIdsActorsByMovie(int id) {
        return jdbcTemplate.query("select actor_id from movies_actors WHERE movie_id=?",
                BeanPropertyRowMapper.newInstance(ActorsId.class), id);
    }

    public List<Actor> allActors() {
        return jdbcTemplate.query("select * from actors", BeanPropertyRowMapper.newInstance(Actor.class));
    }

    public void saveMovie(Movie movie) {
        keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO movies(name, date, description, genre, raiting) " +
                "VALUES(:name, :date, :description, :genre, :raiting);";

        Map<String, Object> params = new HashMap<>();
        params.put("name", movie.getName());
        params.put("date", movie.getDate());
        params.put("description",movie.getDescription());
        params.put("genre", movie.getGenre());
        params.put("raiting", movie.getRaiting());

        int rowsAffected = namedParameterJdbcTemplate.update(sql,
                new MapSqlParameterSource(params),
                keyHolder, new String[] { "id" });

        Integer movieId = keyHolder.getKey().intValue();
        List<String> actors = movie.getActors();

        List<Actor> actorByMovie = mapActor.addActorByMovie(actors);

        mapActor.addActorToMovie(actorByMovie, movieId);

    }

    //Метод для добавления данных
    public ActorsId saveActorsByMovie(ActorsId actor, int movieId) {
        if (actor != null) {
            jdbcTemplate.update("INSERT INTO movies_actors(movie_id, actor_id) VALUES (?, ?)",
                    movieId,
                    actor.getActorId());
        }
        return actor;
    }
    public List<ActorsId> saveActor(List<Actor> actors) {

        keyHolder = new GeneratedKeyHolder();

        List<ActorsId> id = new ArrayList<>();

        String sql = "INSERT INTO actors(name, lastname) VALUES(:name, :lastname)";
        Map<String, Object> params = new HashMap<>();

        actors.forEach(actor -> {
            if (actor != null) {
                params.put("name", actor.getName());
                params.put("lastname", actor.getLastname());

                namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params),
                        keyHolder, new String[] { "id" });
                ActorsId actorsId =  new ActorsId();
                        actorsId.setActorId(keyHolder.getKey().intValue());
                id.add(actorsId);

            }
        });
        return id;
    }
    public void updateMovie(int id, Movie updateMovie) {
        jdbcTemplate.update("UPDATE movies SET name=?, date=?, description=?, genre=?, raiting=? WHERE id=?",
                updateMovie.getName(),
                updateMovie.getDate(),
                updateMovie.getDescription(),
                updateMovie.getGenre(),
                updateMovie.getRaiting(), id);

        List<String> actors = updateMovie.getActors();
        List<Actor> actorByMovie = mapActor.addActorByMovie(actors);
        List<ActorsId> actorsIds = getIdsActorsByMovie(id);

        if (actorsIds.size() < actorByMovie.size()) {
            mapActor.addActorToMovie(actorByMovie, id);
        }
        else {
            mapActor.addIdtoIdACtor(actorsIds, actorByMovie);
        }
    }
    public void updateActors(Actor actor) {
        jdbcTemplate.update("UPDATE actors SET name=?, lastname=? WHERE id=?",
                actor.getName(),
                actor.getLastname(),
                actor.getId());
    }
    public void deleteMovie(int id) {
        List<ActorsId> actorsIds = getIdsActorsByMovie(id);
        jdbcTemplate.update("DELETE FROM movies_actors where movie_id=?", id);
        actorsIds.stream()
                .forEach(actorsId -> deleteActor(actorsId.getActorId()));
        jdbcTemplate.update("DELETE FROM movies WHERE id=?", id);
    }
    public void deleteActor(int id) {
        jdbcTemplate.update("DELETE FROM actors WHERE id=?", id);
    }
}