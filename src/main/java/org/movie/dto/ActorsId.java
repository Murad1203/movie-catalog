package org.movie.dto;

public class ActorsId {
    private Integer actorId;

    public ActorsId() {
    }

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    @Override
    public String toString() {
        return "ActorsId{" +
                "actorId=" + actorId +
                '}';
    }
}
