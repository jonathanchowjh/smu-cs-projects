package entities;

import java.time.Instant;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import helpers.ReplyComparator;

public class Post {
    private int id;
    private String author;
    private String forUser;
    private String post;
    protected ArrayList<String> likes;
    protected ArrayList<String> dislikes;
    private ArrayList<User> tagged;
    protected ArrayList<Reply> replies;
    protected Instant postTime;
    private boolean isGift;
    private boolean isGiftAccepted;

    public Post() {
        
    }

    // [dt, post, dst_username, pid, username], fetching post from db
    public Post(String author, String forUser, String post, Timestamp dt, int pid, ArrayList<String> likes, ArrayList<String> dislikes) {
        this.author = author;
        this.forUser = forUser;
        this.post = post;
        this.postTime = dt.toInstant();
        this.id = pid;
        this.likes = likes;
        this.dislikes = dislikes;
        this.isGift = false;
    }

    // creating a new post
    public Post(String author, String forUser, String post) {
        this.author = author;
        this.forUser = forUser;
        this.post = post;
        this.postTime = Instant.now();
        this.likes = new ArrayList<String>();
        this.dislikes = new ArrayList<String>();
        this.replies = new ArrayList<Reply>();
        this.isGift = false;
    }

    // gift post constructor
    public Post(String gifter, String giftee, String crop, Instant dt) {
        this.author = gifter;
        this.forUser = giftee;
        this.post = crop;
        this.postTime = dt;
        this.likes = new ArrayList<String>();
        this.dislikes = new ArrayList<String>();
        this.replies = new ArrayList<Reply>();
        this.isGift = true;
        this.isGiftAccepted = false;
    }

    public String toString() {
        return this.author + ": " + this.post + "\n[ " + this.likes.size() + " likes, " + this.dislikes.size() + " dislikes ]";
    }

    public String giftPostToString() {
        return this.author + ": Here is a bag of " + this.post + " seeds for you. - City Farmers\n[ " + this.likes.size() + " likes, " + this.dislikes.size() + " dislikes ]";
    }

    public String getAuthor() {
        return this.author;
    }

    public String getPost() {
        return this.post;
    }

    public int getID() {
        return this.id;
    }

    public ArrayList<Reply> getReplies() {
        return this.replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public void setDislikes(ArrayList<String> dislikes) {
        this.dislikes = dislikes;
    }

    public ArrayList<String> getLikes() {
        return this.likes;
    }

    public ArrayList<String> getDislikes() {
        return this.dislikes;
    }

    public Instant getPostTime() {
        return this.postTime;
    }

    public boolean isGiftPost() {
        return this.isGift;
    }

    public boolean isGiftPostAccepted() {
        return this.isGiftAccepted;
    }
}