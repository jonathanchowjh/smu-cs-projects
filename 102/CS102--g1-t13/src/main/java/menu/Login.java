package menu;

import java.util.Scanner;
import entities.User;
import controller.UserController;

public class Login {
  private User user;
  private UserController userCtrl;
  private MainMenu mm;

  public Login() {
    this.userCtrl = new UserController();
  }

  public void login() {
    Scanner sc = new Scanner(System.in);
    System.out.print("\n== Social Magnet :: Login ==\r\nEnter your username > ");
    String username = sc.nextLine();
    System.out.print("Enter your password > ");
    String password = sc.nextLine();
    this.user = userCtrl.checkUser(username);

    if (this.user == null) {
      System.out.println("User not found!\n");
      return;
    }
    
    while (!user.getPassword().equals(password)) {
      System.out.println("Incorrect password\n");
      System.out.print("Enter your password > ");
      password = sc.nextLine();
      if (password.equals("M")) {
        return;
      }
    }

    this.mm = new MainMenu(this.user);
    mm.mainMenu();
  }
}