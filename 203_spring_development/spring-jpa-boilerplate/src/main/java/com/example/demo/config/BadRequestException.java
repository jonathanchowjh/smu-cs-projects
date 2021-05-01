package com.example.demo.config;

import java.lang.Exception;

public class BadRequestException extends Exception {
  public BadRequestException(String err) {
    super(err);
  }
}