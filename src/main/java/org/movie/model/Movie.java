package org.movie.model;

import java.util.Date;
import java.util.List;


public class Movie {


    private int id;
    private String name;
    private Date date;
    private String description;
    private String genre;
    private List<Actors> actors;
    private double raiting;

    public Movie() {
    }


    public Movie(String name, Date date, String description, String genre, List<Actors> actors, double raiting) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.genre = genre;
        this.actors = actors;
        this.raiting = raiting;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Actors> getActors() {
        return actors;
    }

    public void setActors(List<Actors> actors) {
        this.actors = actors;
    }

    public double getRaiting() {
        return raiting;
    }

    public void setRaiting(double raiting) {
        this.raiting = raiting;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", actors=" + actors +
                ", raiting=" + raiting +
                '}';
    }
}
