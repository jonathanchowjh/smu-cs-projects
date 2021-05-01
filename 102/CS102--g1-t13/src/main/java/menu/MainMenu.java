package menu;

import entities.User;
import java.util.Scanner;
import start.Menu;

public class MainMenu {
    private static User user;
    private static Newsfeed newsfeed;
    private static Wall wall;
    private static FriendsMenu friends;
    private static CityFarmers cf;
    private static Menu menu;

    public MainMenu(User user) {
        this.user = user;
        this.newsfeed = new Newsfeed(this.user);
        this.wall = new Wall(this.user);
        this.friends = new FriendsMenu(this.user);
        this.cf = new CityFarmers(this.user);
        this.menu = new Menu();
    }

    public static void mainMenu() {
        int op = 0;
        while (op != 5) {
            System.out.print("\n== Social Magnet :: Main Menu ==\r\nWelcome," +  user.getName() + "\r\n1. News Feed\r\n2. My Wall\r\n3. My Friends\r\n4. City Farmers\r\n5. Logout\r\nEnter your choice > ");
            Scanner sc = new Scanner(System.in);
            op = sc.nextInt();
            if (op == 1) newsfeed.show();
            if (op == 2) wall.show();
            if (op == 3) friends.showFriends();
            if (op == 4) cf.startGame();
        }
        menu.start();
    }
}