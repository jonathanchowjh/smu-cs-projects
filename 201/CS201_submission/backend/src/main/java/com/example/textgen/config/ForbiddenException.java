package com.example.textgen.config;

import java.lang.Exception;

public class ForbiddenException extends Exception {
  public ForbiddenException(String err) {
    super(err);
  }
}