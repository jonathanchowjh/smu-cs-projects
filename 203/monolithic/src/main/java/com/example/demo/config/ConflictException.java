package com.example.demo.config;

import java.lang.Exception;

public class ConflictException extends Exception {
  public ConflictException(String err) {
    super(err);
  }
}