package menu;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.Collections;
import entities.*;
import helpers.PostComparator;
import helpers.ReplyComparator;
import controller.PostsController;
import controller.UserController;
import java.time.Instant;
import java.sql.Timestamp;
import helpers.M;

public class PostsMenu {
    private User user;
    private PostsController postsCtrl;
    private UserController userCtrl;

    public PostsMenu(User user) {
        this.user = user;
        this.postsCtrl = new PostsController();
        this.userCtrl = new UserController();
    }

    // display posts
    public void displayPosts(ArrayList<Post> posts) {
        Collections.sort(posts, new PostComparator());
        if (posts == null || posts.size() == 0) {
            return;
        }
    
        int counter = 0;
    
        // print posts
        for (Post post : posts) {
            
            if (counter >= 5) {
                break;
            }
            counter++;
            System.out.print(counter + ". ");
            if (!post.isGiftPost()) {
                System.out.println(post.toString());
            } else {
                if(!post.isGiftPostAccepted()) {
                    System.out.println(post.giftPostToString());
                }
            }
            
            int reply_counter = 0;
            ArrayList<Reply> replies = postsCtrl.fetchRepliesFromDB(post.getID());
            post.setReplies(replies);
            Collections.sort(replies, new ReplyComparator());
            for (Reply reply : replies) {
                if (reply_counter >= 3) {
                    break;
                }
                reply_counter++;
                System.out.print("\t" + counter + "." + reply_counter + " ");
                System.out.println(reply.toString());
            }
        
            System.out.println();
        }
    }

    // writes new user-created post to DB
    public void writePost(String text, String poster, String dst_user) {
        Post newPost = new Post(poster, dst_user, text);
        // posts.add(newPost);
        postsCtrl.writePostToDB(poster, dst_user, text);
    }

    // display threads
    public void displayThread(Post post, User user) {
        System.out.println("\n== Social Magnet :: View a Thread ==");
        System.out.println("1 " + post.getAuthor() + ": " + post.getPost());

        if (post.getReplies() == null) {
            post.setReplies(postsCtrl.fetchRepliesFromDB(post.getID()));
        }

        for (int i = 1; i <= post.getReplies().size(); i++) {
            System.out.println("\t1." + i + " " + post.getReplies().get(i-1).toString());
        }

        ArrayList<String> likers = postsCtrl.fetchLDFromDB(post.getID(), 1);
        ArrayList<String> dislikers = postsCtrl.fetchLDFromDB(post.getID(), 0);
        post.setLikes(likers);
        post.setDislikes(dislikers);

        System.out.println("\nWho likes this post:");
        for (int i = 1; i <= post.getLikes().size(); i++) {
            String liker = post.getLikes().get(i-1);
            System.out.println("\t" + i + ". " + liker);
        }

        System.out.println("\nWho dislikes this post:");
        for (int i = 1; i <= post.getDislikes().size(); i++) {
            String disliker = post.getDislikes().get(i-1);
            System.out.println("\t" + i + ". " + disliker);
        }

        // threadAction(post, user);
    }

    public void threadAction(Post post, User accessor) {
        Scanner sc = new Scanner(System.in);
        String op = "";
        while (!op.equals("M")) {
            System.out.print("\n[M]ain | [K]ill | [R]eply | [L]ike | [D]islike > ");
            op = sc.nextLine();

            if (op.equals("K")) {
                postsCtrl.killPost(post.getID(), accessor.getUsername());
                return;
            }

            if (op.equals("R")) {
                System.out.print("Enter your message > ");
                String reply = sc.nextLine();
                postsCtrl.setReply(post.getID(), accessor.getUsername(), reply);
                // displayThread(post, accessor);
            }

            if (op.equals("L")) {
                if (post.getDislikes().contains(accessor.getUsername())) {
                    post.getDislikes().remove(accessor.getUsername());
                }
                postsCtrl.setLikesDislikes(post.getID(), accessor.getUsername(), true);
                post.getLikes().add(accessor.getUsername());
                displayThread(post, accessor);
            }
            if (op.equals("D")) {
                if (post.getLikes().contains(accessor.getUsername())) {
                    post.getLikes().remove(accessor.getUsername());
                }
                postsCtrl.setLikesDislikes(post.getID(), accessor.getUsername(), false);
                post.getDislikes().add(accessor.getUsername());
                displayThread(post, accessor);
            }
        }
        MainMenu.mainMenu();
    }

    // checks if arraylist contains post
    public boolean checkExist(ArrayList<Post> posts, Post post) {
        for (Post p : posts) {
            if (p.getID() == post.getID()) {
                return true;
            }
        }
        return false;
    }
}