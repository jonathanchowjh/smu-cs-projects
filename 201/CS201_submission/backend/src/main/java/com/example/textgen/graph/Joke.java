package com.example.textgen.graph;

import com.example.textgen.config.BadRequestException;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Joke {
  private String joke;
  private String firstWord;
  private Integer maxLength;
  
  public Joke() {
    this.joke = "";
    this.firstWord = "";
    this.maxLength = 0;
  }

  @Override
  public String toString() {
    return "[ " + this.joke + " | " + this.firstWord + " | " + this.maxLength + " ]";
  }

  public String getJoke() { return this.joke; }
  @JsonProperty("first_word")
  public String getFirstWord() {
    if (this.firstWord == null || this.firstWord.trim().isBlank()) return "";
    return this.firstWord.trim();
  }
  @JsonProperty("max_length")
  public Integer getMaxLength() {
    if (this.maxLength == null) return 10;
    return this.maxLength;
  }

  public void setJoke(String joke) { this.joke = joke; }
  @JsonProperty("first_word")
  public void setFirstWord(String firstWord) { this.firstWord = firstWord; }
  @JsonProperty("max_length")
  public void setMaxLength(Integer maxLength) { this.maxLength = maxLength; }

  public String[] getWords() throws BadRequestException {
    if (this.joke == null || this.joke.isEmpty()) throw new BadRequestException("Joke Not Found in Request");
    return this.joke.split(" ");
  }
}