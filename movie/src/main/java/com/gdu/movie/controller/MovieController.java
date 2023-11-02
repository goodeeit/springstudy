package com.gdu.movie.controller;

import org.springframework.web.bind.annotation.RestController;

import com.gdu.movie.service.MovieService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MovieController {

  private final MovieService movieService;
  
}
