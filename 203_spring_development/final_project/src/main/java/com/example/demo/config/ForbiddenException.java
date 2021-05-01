package com.example.demo.config;

import java.lang.Exception;

public class ForbiddenException extends Exception {
  public ForbiddenException(String err) {
    super(err);
  }
}