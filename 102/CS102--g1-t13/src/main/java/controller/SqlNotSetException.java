package controller;

public class SqlNotSetException extends Exception {
  public SqlNotSetException(String err) {
    super(err);
  }
}