package com.example.demo.config;

import java.lang.Exception;

public class UnauthorizedException extends Exception {
  public UnauthorizedException(String err) {
    super(err);
  }
}