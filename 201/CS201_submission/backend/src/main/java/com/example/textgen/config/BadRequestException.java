package com.example.textgen.config;

import java.lang.Exception;

public class BadRequestException extends Exception {
  public BadRequestException(String err) {
    super(err);
  }
}