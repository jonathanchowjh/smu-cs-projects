package menu;

import entities.User;
import java.util.Scanner;
import java.util.ArrayList;
import controller.UserController;
import controller.PostsController;
import controller.FriendsController;
import menu.PostsMenu;
import entities.Post;
import helpers.M;

public class Newsfeed {

    private User user;
    private UserController userCtrl;
    private PostsController postsCtrl;
    private PostsMenu postsMenu;
    private ArrayList<User> friends;
    private ArrayList<Post> newsfeed;

    public Newsfeed(User user) {
        this.user = user;
        this.userCtrl = new UserController();
        this.postsCtrl = new PostsController();
        this.postsMenu = new PostsMenu(user);
        this.friends = userCtrl.getFriends(user.getUsername());
    }

    public void show() {
        System.out.println("== Social Magnet :: News Feed ==");

        // posts on his own wall / friend's wall, ie. dst_username = user or dst_username = friend: 
        // getPostsExclusive, getPostsOnFriend, getPostsReceived

        newsfeed = new ArrayList<>();
        
        for (Post post : postsCtrl.getPostsExclusive(user)) {
            if (!postsMenu.checkExist(newsfeed, post)) {
                newsfeed.add(post);
            }
        }
        for (Post post : postsCtrl.getPostsReceived(user)) {
            if(!postsMenu.checkExist(newsfeed, post)) {
                newsfeed.add(post);
            }  
        }

        for (User friend : friends) {
            for (Post post : postsCtrl.getPostsOnFriend(user, friend)) {
                if(!postsMenu.checkExist(newsfeed, post)) {
                    newsfeed.add(post);
                }  
            }
        }

        postsMenu.displayPosts(newsfeed);
        NewsfeedAction();
    }

    public void NewsfeedAction() {
        Scanner sc = new Scanner(System.in);
        String op = "";

        while (!op.equals("M")) {
            System.out.print("[M]ain | [T]hread > ");
            op = sc.nextLine();

            if (M.chAt(op, 0) == 'T') {
                int index = Integer.parseInt(op.substring(1, op.length())) - 1;
                postsMenu.displayThread(newsfeed.get(index), user);
                postsMenu.threadAction(newsfeed.get(index), user);
                show();
            }
        }
        MainMenu.mainMenu();
    }

}