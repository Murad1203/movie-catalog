package org.movie.dao;

import org.movie.dto.ActorsId;
import org.movie.model.Actors;
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
import java.util.stream.Collectors;

@Repository
public class MovieDAO {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private GeneratedKeyHolder keyHolder;

    @Autowired
    public MovieDAO(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
    public List<ActorsId> getIdsActorsByMovie(int id) {
        return jdbcTemplate.query("select actor_id from movies_actors WHERE movie_id=?", BeanPropertyRowMapper.newInstance(ActorsId.class), id);
    }
    public void saveMovie(Movie movie) {
        keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO movies(name, date, description, genre, raiting) VALUES(:name, :date, :description, :genre, :raiting);";

        Map<String, Object> params = new HashMap<>();
        params.put("name", movie.getName());
        params.put("date", movie.getDate());
        params.put("description",movie.getDescription());
        params.put("genre", movie.getGenre());
        params.put("raiting", movie.getRaiting());

        int rowsAffected = namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder, new String[] { "id" });

        Integer movieId = keyHolder.getKey().intValue();
        List<String> actors = movie.getActors();
        List<Actors> actorByMovie = new ArrayList<>();

        actors.stream()
                .map(e -> actorByMovie.add(mapActorDtoToDomainModel(e)))
                .collect(Collectors.toList());

        //Метод для добавления данных
        List<ActorsId> actorId = saveActor(actorByMovie);


        actorId.stream()
                .map(actorsId -> saveActorsByMovie(actorsId, movieId))
                .collect(Collectors.toList());
    }
    private Actors mapActorDtoToDomainModel(String actorStr) {

        var actorSplitted = actorStr.split(" ");
        String name = actorSplitted[0];
        String surname = actorSplitted[1];

        Actors actor = new Actors();
        actor.setName(name);
        actor.setLastname(surname);
        return actor;
    }
    private ActorsId saveActorsByMovie(ActorsId actor, int movieId) {
        if (actor != null) {
            jdbcTemplate.update("INSERT INTO movies_actors(movie_id, actor_id) VALUES (?, ?)", movieId, actor.getActorId());
        }
        return actor;
    }
    public List<ActorsId> saveActor(List<Actors> actors) {

        keyHolder = new GeneratedKeyHolder();

        List<ActorsId> id = new ArrayList<>();

        String sql = "INSERT INTO actors(name, lastname) VALUES(:name, :lastname)";
        Map<String, Object> params = new HashMap<>();

        actors.forEach(actor -> {
            if (actor != null) {
                params.put("name", actor.getName());
                params.put("lastname", actor.getLastname());

                namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder, new String[] { "id" });
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
        System.out.println(" " + actors);
        List<Actors> actorByMovie = new ArrayList<>();

        actors.stream()
                .map(e -> actorByMovie.add(mapActorDtoToDomainModel(e)))
                .collect(Collectors.toList());

        List<ActorsId> actorsIds = getIdsActorsByMovie(id);
        Actors actortest = new Actors();

        if (actorsIds.size() < actorByMovie.size()) {
            List<ActorsId> actorId = saveActor(actorByMovie);
            actorId.stream()
                    .map(actorsId -> saveActorsByMovie(actorsId, id))
                    .collect(Collectors.toList());
        }
        else {
            for (int i = 0; i < actorsIds.size(); i++) {

                actortest.setId(actorsIds.get(i).getActorId());
                actortest.setName(actorByMovie.get(i).getName());
                actortest.setLastname(actorByMovie.get(i).getLastname());
                updateActors(actortest);
            }
        }
    }

    public void updateActors(Actors actor) {
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