package entities.farmers;

public class Rank {
  private String name;
  private int xp;
  private int plots;
  public Rank(String name, int xp, int plots) {
    this.name = name;
    this.xp = xp;
    this.plots = plots;
  }
  @Override
  public String toString() {
    return "{name=" + this.name + ", xp=" + this.xp + ", plot=" + this.plots + "}";
  }
  public String getName() {
    return this.name;
  }
  public int getXp() {
    return this.xp;
  }
  public int getPlots() {
    return this.plots;
  }
}