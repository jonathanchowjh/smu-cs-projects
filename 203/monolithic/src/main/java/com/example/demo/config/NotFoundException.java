package com.example.demo.config;

import java.lang.Exception;

public class NotFoundException extends Exception {
  public NotFoundException(String err) {
    super(err);
  }
}