package start;

import dao.*;
import entities.*;
import java.util.*;
import java.sql.Timestamp;
import helpers.*;
import controller.*;

public class App {
  public static void main(String[] args) {
    Menu menu = new Menu();
    menu.start();
    // try {
    //   // UserController uCtrl = new UserController();
    //   // PostsController ctrl = new PostsController();
    //   // ctrl.getPostsForFriend(uCtrl.checkUser("jon"));
    //   UserDAO udao = new UserDAO();
    //   Map<String, String> u = udao.getUser("jon");
    //   boolean passwordValid = udao.checkPassword("jon", "1111");
    //   ArrayList<Map<String, String>> f = udao.getFriends("jon");
    //   ArrayList<Map<String, String>> frs = udao.getFriendsRequestS("jon");
    //   ArrayList<Map<String, String>> frr = udao.getFriendsRequestR("jon");
    //   // ArrayList<Map<String, String>> register = udao.register("new", "new new", "1111");
    //   // ArrayList<Map<String, String>> updateUser = udao.updateUser("jonnnn", 1, 2);
    //   // ArrayList<Map<String, String>> setFriends = udao.setFriends("new", "jon");
    //   // ArrayList<Map<String, String>> setFriendsReq = udao.setFriendsReq("jiemi", "jon"); // if (unique sender/receipient) add else delete

    //   PostDAO pdao = new PostDAO();
    //   ArrayList<Map<String, String>> ps = pdao.getPostUser("jon");
    //   ArrayList<Map<String, String>> pd = pdao.getPostDest("jiemi");
    //   ArrayList<Map<String, String>> l = pdao.getLikes(2);
    //   ArrayList<Map<String, String>> r = pdao.getReplies(2);
    //   ArrayList<Map<String, String>> g = pdao.getGifts("jon");
    //   ArrayList<Map<String, String>> gg = pdao.getGiftsGifter("jon");
    //   // ArrayList<Map<String, String>> k = pdao.setKill(1, "jon");
    //   // ArrayList<Map<String, String>> createGifts = pdao.createGifts("jon", "jiemi", "Papaya");
    //   // ArrayList<Map<String, String>> setGifts = pdao.setGifts(1, true);
    //   // ArrayList<Map<String, String>> setReplies = pdao.setReplies(1, "jon", "hihi");
    //   // ArrayList<Map<String, String>> deleteReplies = pdao.deleteReplies(2);
    //   // ArrayList<Map<String, String>> setLike = pdao.setLike(2, "jiemi", true);
    //   // ArrayList<Map<String, String>> setPosts = pdao.setPosts("jon", "NULL", "hihihi");
    //   // ArrayList<Map<String, String>> deletePosts = pdao.deletePosts(3);

    //   FarmerDAO fdao = new FarmerDAO();
    //   ArrayList<Map<String, String>> lands = fdao.getLands("jon");
    //   ArrayList<Map<String, String>> seeds = fdao.getSeeds("jon");
    //   // ArrayList<Map<String, String>> setLand = fdao.setLand(1, "jon", "Papaya");
    //   // ArrayList<Map<String, String>> createLand = fdao.createLand("jon", "Papaya");
    //   // ArrayList<Map<String, String>> setSeeds = fdao.setSeeds("jon", "Papaya", 3); // if (unique user/seed) {insert} else {edit}
    //   System.out.println("" + k);
    // } catch (Exception e) {
    //   e.printStackTrace();
    // }
  }
}