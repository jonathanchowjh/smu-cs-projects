package entities;

public class User {
  private String name;
  private String username;
  private String password;
  private int gold;
  private int xp;
  public User(String name, String username, String password, int gold, int xp) {
    this.name = name;
    this.username = username;
    this.password = password;
    this.gold = gold;
    this.xp = xp;
  }
  @Override
  public String toString() {
    return this.username;
  }
  public String getName() {
    return this.name;
  }
  public String getUsername() {
    return this.username;
  }
  public String getPassword() {
    return this.password;
  }
  public int getGold() {
    return this.gold;
  }
  public int getXp() {
    return this.xp;
  }
}