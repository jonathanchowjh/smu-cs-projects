package com.example.demo.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
  @ExceptionHandler
  void handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) throws IOException {
    response.resetBuffer();
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.setHeader("Content-Type", "application/json");
    response.getOutputStream().print(String.format("{\"error\":\"%s\"}", "Validation Error: Invalid Input"));
    response.flushBuffer();
  }

  @ExceptionHandler
  void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
    response.resetBuffer();
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.setHeader("Content-Type", "application/json");
    response.getOutputStream().print(String.format("{\"error\":\"%s\"}", e.getMessage()));
    response.flushBuffer();
  }

  @ExceptionHandler
  void handleIllegalStateException(IllegalStateException e, HttpServletResponse response) throws IOException {
    response.resetBuffer();
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setHeader("Content-Type", "application/json");
    response.getOutputStream().print(String.format("{\"error\":\"%s\"}", e.getMessage()));
    response.flushBuffer();
  }

  @ExceptionHandler
  void handleBadRequestException(BadRequestException e, HttpServletResponse response) throws IOException {
    response.resetBuffer();
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.setHeader("Content-Type", "application/json");
    response.getOutputStream().print(String.format("{\"error\":\"%s\"}", e.getMessage()));
    response.flushBuffer();
  }

  @ExceptionHandler
  void handleForbiddenException(ForbiddenException e, HttpServletResponse response) throws IOException {
    response.resetBuffer();
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setHeader("Content-Type", "application/json");
    response.getOutputStream().print(String.format("{\"error\":\"%s\"}", e.getMessage()));
    response.flushBuffer();
  }
  
  @ExceptionHandler
  void handleNotFoundException(NotFoundException e, HttpServletResponse response) throws IOException {
    response.resetBuffer();
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    response.setHeader("Content-Type", "application/json");
    response.getOutputStream().print(String.format("{\"error\":\"%s\"}", e.getMessage()));
    response.flushBuffer();
  }

  @ExceptionHandler
  void handleNotFoundException(ConflictException e, HttpServletResponse response) throws IOException {
    response.resetBuffer();
    response.setStatus(HttpServletResponse.SC_CONFLICT);
    response.setHeader("Content-Type", "application/json");
    response.getOutputStream().print(String.format("{\"error\":\"%s\"}", e.getMessage()));
    response.flushBuffer();
  }

  @ExceptionHandler
  void handleNotFoundException(UnauthorizedException e, HttpServletResponse response) throws IOException {
    response.resetBuffer();
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setHeader("Content-Type", "application/json");
    response.getOutputStream().print(String.format("{\"error\":\"%s\"}", e.getMessage()));
    response.flushBuffer();
  }
}