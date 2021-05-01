package entities.farmers;

import java.lang.Math;
import java.util.ArrayList;

public class Crop {
  private String name;
  private int cost; // gold
  private int time; // minutes
  private int xp;
  private int minYield;
  private int maxYield;
  private int salePrice;
  public Crop(String name, int cost, int time, int xp, int minYield, int maxYield, int salePrice) {
    this.name = name;
    this.cost = cost;
    this.time = time;
    this.xp = xp;
    this.minYield = minYield;
    this.maxYield = maxYield;
    this.salePrice = salePrice;
  }
  @Override
  public String toString() {
    return "{name=" + this.name + ", cost=" + this.cost + ", time=" + this.time + ", xp=" + this.xp + ", minYield=" + this.minYield + ", maxYield=" + this.maxYield + ", salePrice=" + this.salePrice + "}";
  }
  public String getName() {
    return this.name;
  }
  public int getCost() {
    return this.cost;
  }
  public int getTime() {
    return this.time;
  }
  public int getXp() {
    return this.xp;
  }
  public int getMinYield() {
    return this.minYield;
  }
  public int getMaxYield() {
    return this.maxYield;
  }
  public int getSalePrice() {
    return this.salePrice;
  }
  public int getRandYield() {
    return (int) (this.minYield + Math.random() * (this.maxYield - this.minYield));
  }
}