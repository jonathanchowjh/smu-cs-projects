package entities.farmers;

import helpers.*;
import entities.*;
import java.util.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Land {
  private int lid;
  private String username;
  private Crop crop;
  private Timestamp startTime;
  public Land(Map<String, String> land) {
    this.lid = Integer.parseInt(land.get("lid"));
    this.username = land.get("username");
    this.crop = Data.getCrop(land.get("crop"));
    this.startTime = M.toTimestamp(land.get("startTime"));
  }
  @Override
  public String toString() {
    return "{lid=" + this.lid + ", username=" + this.username + ", crop=" + this.crop + ", startTime=" + this.startTime + "}"; 
  }
  public int getLid() {
    return this.lid;
  }
  public String getUsername() {
    return this.username;
  }
  public Crop getCrop() {
    return this.crop;
  }
  public Timestamp getStartTime() {
    return this.startTime;
  }
  public long getPercentCompleted() {
    long minsElapsed = this.startTime.toInstant().until(Instant.now(),ChronoUnit.MINUTES);
    long percent = minsElapsed * 100 / crop.getTime();
    // System.out.println("STARTTIME: " + this.startTime);
    // System.out.println("NOW: " + Instant.now());
    // System.out.println("PERCENT: " + percent + " // CROP TIME: " + crop.getTime() + " // MINS: " + minsElapsed);
    return percent;
  }
  // --- Percent Meter ---
  // if (percent >= 200) return [  wilted  ]
  // Number of '#' = percentage  / 10 (with cap at 10 '#'s)
  public String percentMeter(long percent) {
    if (percent >= 200) return "[  wilted  ]";
    char[] str = {'[','-','-','-','-','-','-','-','-','-','-',']'};
    long percentWCap = percent > 100 ? 100 : percent;
    for (int i = 0; i < percentWCap / 10; i++) {
      str[i + 1] = '#';
    }
    return String.valueOf(str);
  }
}