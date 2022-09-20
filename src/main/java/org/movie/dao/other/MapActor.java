package org.movie.dao.other;

import org.movie.dao.MovieDAO;
import org.movie.dto.ActorsId;
import org.movie.model.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class MapActor {

    @Autowired
    private MovieDAO movieDAO;

    public Actor mapActorDtoToDomainModel(String actorStr) {
        var actorSplitted = actorStr.split(" ");
        String name = actorSplitted[0];
        String surname = actorSplitted[1];

        Actor actor = new Actor();
        actor.setName(name);
        actor.setLastname(surname);
        return actor;
    }


    public void addActorToMovie(List<Actor> actorByMovie, int movieId) {

        List<ActorsId> actorId = movieDAO.saveActor(actorByMovie);

        actorId.stream()
                .map(actorsId -> movieDAO.saveActorsByMovie(actorsId, movieId))
                .collect(Collectors.toList());
    }

    public List<Actor> addActorByMovie(List<String> actors) {

        List<Actor> actorByMovie = new ArrayList<>();
        actors.stream()
                .map(e -> actorByMovie.add(mapActorDtoToDomainModel(e)))
                .collect(Collectors.toList());

        return actorByMovie;
    }

    public Actor addIdtoIdACtor(List<ActorsId> actorsIds, List<Actor> actorByMovie) {
        Actor actortest = new Actor();
        for (int i = 0; i < actorsIds.size(); i++) {
            actortest.setId(actorsIds.get(i).getActorId());
            actortest.setName(actorByMovie.get(i).getName());
            actortest.setLastname(actorByMovie.get(i).getLastname());
            movieDAO.updateActors(actortest);
        }
        return actortest;
    }


}
