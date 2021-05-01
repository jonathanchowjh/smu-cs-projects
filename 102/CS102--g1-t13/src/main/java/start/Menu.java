package start;

import java.util.Scanner;
import menu.*;
import entities.User;
import start.Menu;

public class Menu {
  private User user;
  private Registration reg;
  private Login login;

  public Menu() {
    this.reg = new Registration();
    this.login = new Login();
    this.user = new User("name", "username", "password", 100, 100);
  }

  public void start() {
    int op = 0;
    while (op != 3) {
      Scanner sc = new Scanner(System.in);
      System.out.print("\n== Social Magnet :: Welcome ==\r\nGood morning, anonymous!\r\n1. Register\r\n2. Login\r\n3. Exit\r\nEnter your choice > ");
      op = sc.nextInt();
      System.out.println();
      if (op == 1) this.reg.register();
      if (op == 2) this.login.login();
    }
    System.exit(0);
  }
}