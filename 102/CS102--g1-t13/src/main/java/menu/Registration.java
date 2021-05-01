package menu;
import java.util.*;
import entities.User;
import controller.UserController;


public class Registration {
  private String username;
  private String fullName;
  private String password;
  private UserController userCtrl;

  public Registration() {
    this.userCtrl = new UserController();
  }

  public void register() {
    System.out.print("\n== Social Magnet :: Registration ==\n");
    Scanner sc = new Scanner(System.in);

    String username = "";
    boolean check = false;
      while (!check){
        System.out.print("1. Enter your username > ");
        username = sc.nextLine();
        check = isAlphaNum(username); 
        if (!check){
          System.out.println("Invalid username! Alphanumeric characters only.");
        } else {
          User user = userCtrl.checkUser(username);
          if (user == null){
            check = true;
          } else {
            System.out.println("Username already taken! Please choose a new username!");
            check = false;
          }
        }
      }

    System.out.print("2. Enter your Full name > ");
    String fullname = sc.nextLine();

    //password check
    System.out.print("3. Enter your password > ");
    String password = sc.nextLine();
    boolean passwordcheck = false;

    while (!passwordcheck){
      System.out.print("4. Confirm your password > ");
      String confirmPassword = sc.nextLine();
      passwordcheck = confirmPassword.equals(password);
      if (!passwordcheck){
        System.out.println("Passwords don't match.");
      }
    }

    // add new user to database
    ArrayList<Map<String, String>> arr = userCtrl.addUser(username, fullname, password);
    boolean isAdded = (arr != null);
    if (isAdded) {
      System.out.print(username + ",your account is successfully created!\n\n");
    }
  }

  public static boolean isAlphaNum(String input){
    return input != null && input.matches("^[a-zA-Z0-9]*$");
  }
}