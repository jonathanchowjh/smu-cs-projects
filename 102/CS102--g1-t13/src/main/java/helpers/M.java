package helpers;

import java.util.*;
import java.sql.Timestamp;

public class M {
  public static char chAt(String s, int index) {
    if (s.length() <= index) return ' ';
    return s.charAt(index);
  }
  private static Map<String, String> jsonObj(String s) {
    Map<String, String> m = new HashMap<>();
    String[] arr = s.split(",");
    for (String str : arr) {
      if (!str.contains(":")) continue;
      String[] keyValue = str.split(":");
      m.put(keyValue[0], keyValue[1]);
    }
    return m;
  }
  public static ArrayList<Map<String, String>> jsonAofObj(String s) {
    ArrayList<Map<String, String>> array = new ArrayList<>();
    String[] arr = s.split("//");
    for (String str : arr) {
      if (!str.contains(":")) continue;
      array.add(jsonObj(str));
    }
    return array;
  }
  public static Timestamp toTimestamp(String str) {
    char[] chars = str.toCharArray();
    chars[10] = ' ';
    chars[13] = ':';
    chars[16] = ':';
    String s = new String(chars);
    return Timestamp.valueOf(s);
  }
  public static boolean isSet(ArrayList<Map<String, String>> arr, String s, int min) {
    return isSet(arr, s, min, 1000);
  }
  public static boolean isSet(ArrayList<Map<String, String>> arr, String s, int min, int max) {
    try {
      if (arr.size() > 0 && arr.get(0).get(s) != null) {
        String value = arr.get(0).get(s);
        int v = Integer.parseInt(value);
        if (v < min || v > max) return false;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}