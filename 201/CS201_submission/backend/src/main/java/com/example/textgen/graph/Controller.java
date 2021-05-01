package com.example.textgen.graph;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;   

import java.util.*;
import java.lang.IllegalArgumentException;
import com.example.textgen.config.BadRequestException;

@RestController
public class Controller {
  Graph graph;

  @Autowired
  public Controller() {
    this.graph = new Graph();
  }

  @GetMapping(path="/sample")
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody String sample() {
    return "Sample Connection";
  }

  @RequestMapping(path="/api/model", method=RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public void insertJoke(@RequestBody Joke joke) throws BadRequestException {
    System.out.println("POST /api/model | " + joke);
    String[] corpus = joke.getWords();            // throws BadRequestException
    for (int i = 0; i < corpus.length; i++) {
      if (i == 0) {
        this.graph.add(null, corpus[i]);
        continue;
      }
      this.graph.add(corpus[i-1], corpus[i]);
    }
    this.graph.add(null, corpus[corpus.length - 1], true);
  }

  @CrossOrigin(origins = "http://localhost:3000") //  consumes={"application/x-www-form-urlencoded"}
  @RequestMapping(value="/api/model/predict", method = RequestMethod.POST)
  public @ResponseBody Joke getModel(@RequestBody Joke req) throws BadRequestException {
    System.out.println("POST /api/model/predict | " + req.getFirstWord() + " | " + req.getMaxLength());
    // if (req.getFirstWord().length() > 20) throw new BadRequestException("Word has to be less than 20 chars");
    Joke joke = new Joke();
    String gen = this.graph.predict(req.getFirstWord(), req.getMaxLength());
    joke.setJoke(gen);
    return joke;
  }
}