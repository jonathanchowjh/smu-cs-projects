package entities.farmers;

import helpers.*;
import entities.*;
import java.util.*;

public class Seed {
  private String username;
  private Crop crop;
  private int quantity;
  public Seed(Map<String, String> seed) {
    this.username = seed.get("username");
    this.crop = Data.getCrop(seed.get("crop"));
    String q = seed.get("quantity");
    this.quantity = Integer.parseInt(q);
  }
  @Override
  public String toString() {
    return "{username=" + this.username + ", crop=" + this.crop + ", quantity=" + this.quantity + "}";
  }
  public String getUsername() {
    return this.username;
  }
  public Crop getCrop() {
    return this.crop;
  }
  public int getQuantity() {
    return this.quantity;
  }
}