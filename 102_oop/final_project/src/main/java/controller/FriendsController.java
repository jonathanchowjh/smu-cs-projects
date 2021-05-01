package controller;

import entities.User;
import java.util.ArrayList;
import java.util.Map;
import dao.UserDAO;
import dao.DAOException;

public class FriendsController {
    private User user;
    private static UserDAO user_dao;

    public FriendsController(User user) {
        this.user = user;
        this.user_dao = new UserDAO();
    }

    // public static ArrayList<String> fetchFriendsFromDB(User forUser) {
    //     ArrayList<String> friends = new ArrayList<>();
    //     try {
    //         ArrayList<Map<String, String>> friendsOfUser = user_dao.getFriends(forUser.getUsername());
    //         for (Map<String, String> map : friendsOfUser) {
    //             friends.add(map.get("username"));
    //         }
    //     }
    //     catch (DAOException e) {
    //         System.out.println("Unable to establish connection to server.");
    //     }
    //     return friends;
    // }

    public static ArrayList<String> fetchRequestsFromDB(User forUser) {
        ArrayList<String> requests = new ArrayList<>();
        try {
            ArrayList<Map<String, String>> friendReqsOfUser = user_dao.getFriendsRequestR(forUser.getUsername());
            for (Map<String, String> map : friendReqsOfUser) {
                requests.add(map.get("username"));
            }
        }
        catch (DAOException e) {
            System.out.println("Unable to fetch friend requests.");
        }
        return requests;
    }

    public static ArrayList<Map<String, String>> setRequest(String sender, String recipient) {
        ArrayList<Map<String, String>> request = null;
        try {
            Map<String, String> findReq = user_dao.getUser(recipient);
            request = user_dao.setFriendsReq(sender, recipient);
        }
        catch (DAOException e) {
            System.out.println("User does not exist.");
        }
        return request;
    }

    public static void setFriend(String u1, String u2) {
        // ArrayList<Map<String, String>> add = null;
        try {
            ArrayList<Map<String, String>> add = user_dao.setFriends(u1, u2);
        }
        catch (DAOException e) {
            // System.out.println("Unable to add/remove friend.");
            System.out.println(e);
        }
    }
}