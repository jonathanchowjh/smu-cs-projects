package menu;

import controller.FriendsController;
import controller.UserController;
import helpers.M;
import entities.User;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class FriendsMenu {
    private User user;
    private ArrayList<User> friends;
    private ArrayList<String> friend_reqs;
    private FriendsController friendsCtrl;
    private UserController userCtrl;
    private Login login;

    public FriendsMenu(User user) {
        this.user = user;
        this.friendsCtrl = new FriendsController(user);
        this.userCtrl = new UserController();
        this.friends = this.userCtrl.getFriends(user.getUsername());
        this.friend_reqs = this.friendsCtrl.fetchRequestsFromDB(user);
    }

    public void showFriends() {
        System.out.println("\n== Social Magnet :: My Friends ==\nWelcome, " + this.user.getUsername() + "!\n");
        System.out.println("My Friends:");
        try {
            for (int i = 1; i <= friends.size(); i++) {
                System.out.println("" + i + ". " + friends.get(i-1));
            }
    
            System.out.println();
            System.out.println("My Requests:");
            for (int i = 1; i <= friend_reqs.size(); i++) {
                System.out.println("" + (i + friends.size()) + ". " + friend_reqs.get(i-1));
            }

            System.out.println();
        }
        catch (NullPointerException e) {
            System.out.println("No friends found :-(");
        }

        action();
    }

    public void showFriendsOfFriends() {
        try {
            for (int i = 1; i <= friends.size(); i++) {
                System.out.println(i + ". " + friends.get(i-1));
            }
    
            System.out.println();
        }
        catch (NullPointerException e) {
            System.out.println("No friends found :-(");
        }
    }

    public void action() {
        Scanner sc = new Scanner(System.in);
        String op = "";

        while (!op.equals("M")) {
            System.out.print("[M]ain | [U]nfriend | re[Q]uest | [A]ccept | [R]eject | [V]iew > ");
            op = sc.nextLine();
            
            if (op.equals("Q")) {
                System.out.print("Enter the username > ");
                String usr = sc.nextLine();

                boolean action = true;

                if (usr.equals(user.getUsername())) {
                    action = false;
                    System.out.println("You can't send a friend request to yourself.\n");
                }

                for (User fr : friends) {
                    if (fr.getUsername().equals(usr)) {
                        System.out.println(usr + " is already a friend.\n");
                        action = false;
                    }
                }
                for (String req : friend_reqs) {
                    if (req.equals(usr)) {
                        System.out.println(usr + " has already sent you a request.\n");
                        action = false;
                    }
                }

                if (action) {
                    ArrayList<Map<String, String>> request = friendsCtrl.setRequest(this.user.getUsername(), usr);
                    if (request.get(0).containsKey("rows_deleted") && request.get(0).get("rows_deleted").equals("1")) {
                        System.out.println("Your friend request to " + usr + " has been rescinded.\n");
                    } else {
                        System.out.println("A friend request is sent to " + usr + "\n");
                    }
                }
                
            }
            if (M.chAt(op, 0) == 'U') {
                int index = Integer.parseInt(op.substring(1, op.length())) - 1;
                try {
                    User toRemove = this.friends.get(index);
                    friends.remove(toRemove);
                    friendsCtrl.setFriend(user.getUsername(), toRemove.getUsername());
                }
                catch (IndexOutOfBoundsException e) {
                    System.out.println("No such friend.\n");
                }   
            }
            if (M.chAt(op, 0) == 'A') {
                int index = Integer.parseInt(op.substring(1, op.length())) - this.friends.size() - 1;
                try {
                    System.out.println(index);
                    String toAccept = this.friend_reqs.get(index);
                    friends.add(userCtrl.checkUser(toAccept));
                    friend_reqs.remove(toAccept);
                    friendsCtrl.setRequest(toAccept, user.getUsername());
                    friendsCtrl.setFriend(user.getUsername(), toAccept);
                }
                catch (IndexOutOfBoundsException e) {
                    System.out.println("No such friend request.\n");
                }  
            }
            if (M.chAt(op, 0) == 'R') {
                int index = Integer.parseInt(op.substring(1, op.length())) - this.friends.size() - 1;
                try {
                    String toReject = this.friend_reqs.get(index);
                    friend_reqs.remove(toReject);
                    ArrayList<Map<String, String>> req = friendsCtrl.setRequest(this.user.getUsername(), toReject);
                    System.out.println(req);
                }
                catch (IndexOutOfBoundsException e) {
                    System.out.println("No such friend request.\n");
                } 
            }
            if (M.chAt(op, 0) == 'V') {
                int index = Integer.parseInt(op.substring(1, op.length())) - 1;
                try {
                    User friend = friends.get(index);
                    Wall friendsWall = new Wall(user, friend);
                    friendsWall.showFriendsWall(friend);
                    showFriends();
                }
                catch (IndexOutOfBoundsException e) {
                    System.out.println("No such friend.\n");
                }
            }
        }
    }

}