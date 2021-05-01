import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Json {
  private String str;
  public Json() {
    this.str = "";
  }
  private void add(String s) {
    this.str = this.str + s;
  }
  public void closeObj() {
    this.str = this.str.substring(0, str.length() - 1);
    this.add("//");
  }
  public void setString(String key, String s) {
    this.add(key + ":" + s + ",");
  }
  public void setInt(String key, int i) {
    this.add(key + ":" + Integer.toString(i) + ",");
  }
  public void setTimestamp(String key, Timestamp timestamp) {
    String str = timestamp.toString();
    char[] chars = str.toCharArray();
    chars[10] = ' ';
    chars[13] = '-';
    chars[16] = '-';
    String date = new String(chars);
    date = date.substring(0, 19);
    this.add(key + ":" + date + ",");
  }
  public String toJsonString() {
    return this.str;
  }
}