package menu;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import entities.*;
import helpers.M;
import helpers.Data;
import helpers.WealthComparator;
import controller.PostsController;
import controller.UserController;

public class Wall {

    private User user;
    private User friend;
    private ArrayList<Post> wall;
    private ArrayList<Gift> gifts;
    private UserController userCtrl;
    private PostsController postsCtrl;
    private PostsMenu postsMenu;

    // viewing own wall
    public Wall(User user) {
        this.user = user;
        this.userCtrl = new UserController();
        this.postsCtrl = new PostsController();
        this.postsMenu = new PostsMenu(user);
        this.gifts = postsCtrl.fetchGiftsFromDB(user.getUsername());
    }

    // viewing friend's wall
    public Wall (User user, User friend) {
        this.user = user;
        this.friend = friend;
        this.userCtrl = new UserController();
        this.postsCtrl = new PostsController();
        this.postsMenu = new PostsMenu(friend);
    }

    public void show() {
        System.out.println("\n== Social Magnet :: My Wall ==\nAbout " + user.getUsername() + "\nFull Name: " + user.getName());

        System.out.print(Data.getRank(user.getXp()).getName() + " Farmer");
        System.out.println(", " + wealthRank(user) + "\n");

        // posts on own wall, ie. username = user, dst_username = user: 
        // getPostsExclusive

        wall = new ArrayList<Post>();

        for (Post post : postsCtrl.getPostsExclusive(user)) {
            if(!postsMenu.checkExist(wall, post)) {
                wall.add(post);
            } 
        }
        // for (Post post : postsCtrl.getPostsReceived(user)) {
        //     if(!postsMenu.checkExist(wall, post)) {
        //         wall.add(post);
        //     } 
        // }
        for (Post post : postsCtrl.giftPosts(postsCtrl.fetchGiftsFromDB(user.getUsername()))) {
            if(!postsMenu.checkExist(wall, post)) {
                wall.add(post);
            } 
        }

        postsMenu.displayPosts(wall);
        WallAction();
    }

    public void WallAction() {
        Scanner sc = new Scanner(System.in);
        String op = "";

        while (!op.equals("M")) {
            System.out.print("[M]ain | [T]hread | [A]ccept Gift | [P]ost > ");
            op = sc.nextLine();

            if (M.chAt(op, 0) == 'T') {
                int index = Integer.parseInt(op.substring(1, op.length())) - 1;
                postsMenu.displayThread(wall.get(index), user);
                postsMenu.threadAction(wall.get(index), user);
                show();
            }

            if (op.equals("A")) {
                for (Gift gift : gifts) {
                    gift.setAccepted(true);
                    postsCtrl.updateGiftStatus(gift.getGID());
                }
                show();
            }

            if (op.equals("P")) {
                // user posts to own wall
                System.out.print("Enter your message > ");
                String message = sc.nextLine();
                postsMenu.writePost(message, user.getUsername(), user.getUsername());
                show();
            }
        }
        MainMenu.mainMenu();
    }

    public void showFriendsWall(User friend) {
        System.out.println("\n== Social Magnet :: " + friend.getName() + "'s Wall ==");
        System.out.println("Welcome, " + user.getName() + "!\n");

        System.out.println("About " + friend.getName() + "\nFull Name: " + friend.getName());

        System.out.print(Data.getRank(friend.getXp()).getName() + " Farmer");
        System.out.println(", " + wealthRank(friend) + "\n");

        // posts on friend's wall, by friend and user, ie. username = friend, dst_username = friend or username = user, dst_username = friend
        // getPostsForFriend, getPostsExclusive(friend)

        ArrayList<Post> friendWall = new ArrayList<>();

        for (Post post : postsCtrl.getPostsExclusive(friend)) {
            if(!postsMenu.checkExist(friendWall, post)) {
                friendWall.add(post);
            }  
        }
        for (Post post : postsCtrl.getPostsForFriend(user, friend)) {
            if(!postsMenu.checkExist(friendWall, post)) {
                friendWall.add(post);
            } 
        }

        postsMenu.displayPosts(friendWall);

        System.out.println("\n" + friend.getName() + "'s Friends");
        FriendsMenu friendsMenu = new FriendsMenu(friend);
        friendsMenu.showFriendsOfFriends();

        FriendsWallAction(friendWall);
    }

    public void FriendsWallAction(ArrayList<Post> friendWall) {
        Scanner sc = new Scanner(System.in);
        String op = "";

        while (!op.equals("M")) {
            System.out.print("[M]ain | [T]hread | [P]ost > ");
            op = sc.nextLine();

            if (M.chAt(op, 0) == 'T') {
                int index = Integer.parseInt(op.substring(1, op.length())) - 1;
                postsMenu.displayThread(friendWall.get(index), user);
                postsMenu.threadAction(friendWall.get(index), user);
                showFriendsWall(friend);
            }
            if (op.equals("P")) {
                System.out.print("Enter your message > ");
                String text = sc.nextLine();
                postsMenu.writePost(text, user.getUsername(), friend.getUsername());
                showFriendsWall(friend);
            }
        }
        MainMenu.mainMenu();
    }

    public String wealthRank(User user) {
        ArrayList<User> wealth = userCtrl.getFriends(user.getUsername());
        wealth.add(user);
        Collections.sort(wealth, new WealthComparator());
        String suffix = "th";
        if ((wealth.indexOf(user) + 1) == 1) return "Richest";
        if ((wealth.indexOf(user) + 1) == 2) suffix = "nd";
        if ((wealth.indexOf(user) + 1) == 3) suffix = "rd";

        return "" + ((wealth.indexOf(user) + 1)) + suffix + " richest";
    }

    public User getUser() {
        return this.user;
    }

    public User getFriend() {
        return this.friend;
    }
}