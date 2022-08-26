package org.movie.dao;

import org.movie.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MovieDAO {


    private JdbcTemplate jdbcTemplate;

    public MovieDAO(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<Movie> allMovies() {
        return jdbcTemplate.query("select * from movies", BeanPropertyRowMapper.newInstance(Movie.class));
    }
}
