package org.movie.controller;

import org.movie.dao.MovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MovieController {

    @Autowired
    private MovieDAO movieDAO;

    @GetMapping
    public String getHello() {
        return "index";
    }

}
