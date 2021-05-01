package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.sql.Timestamp;
import java.time.Instant;
import dao.PostDAO;
import dao.DAOException;
import entities.*;
import helpers.PostComparator;
import helpers.ReplyComparator;
import helpers.M;

public class PostsController {
    private static PostDAO post_dao;

    public PostsController() {
        this.post_dao = new PostDAO();
    }

    public PostDAO getPostDAO() {
        return this.post_dao;
    }

    public void killPost(int pid, String username) {
        try {
            post_dao.setKill(pid, username);
        }
        catch (DAOException e) {
            System.out.println(e);
        }
    }

    public Post createPost(Map<String, String> map) {
        ArrayList<String> likes = fetchLDFromDB(Integer.parseInt(map.get("pid")), 1);
        ArrayList<String> dislikes = fetchLDFromDB(Integer.parseInt(map.get("pid")), 0);

        Timestamp dt = M.toTimestamp(map.get("dt"));

        return (new Post(map.get("username"), map.get("dst_username"), map.get("post"), dt, Integer.parseInt(map.get("pid")), likes, dislikes));
    }

    public ArrayList<Post> getPostsExclusive(User user) {
        // fetches posts user posted to their own wall, username & dst_username = user

        ArrayList<Post> posts = new ArrayList<>();
        ArrayList<String> likes = new ArrayList<>();
        ArrayList<String> dislikes = new ArrayList<>();

        try {
            ArrayList<Map<String, String>> postsExclusive = post_dao.getPostUser(user.getUsername());
            for (Map<String, String> map : postsExclusive) {

                if(map.get("username").equals(map.get("dst_username"))) {
                    posts.add(createPost(map));
                }
            }
        }
        catch (DAOException e) {
            System.out.println(e);
        }
        return posts;
    }

    public ArrayList<Post> getPostsForFriend(User user, User friend) {
        // fetches posts user posted to their friends' walls, username = user & dst_username = friend

        ArrayList<Post> posts = new ArrayList<>();
        ArrayList<String> likes = new ArrayList<>();
        ArrayList<String> dislikes = new ArrayList<>();

        try {
            ArrayList<Map<String, String>> postsForFriend = post_dao.getPostUser(user.getUsername());
            for (Map<String, String> map : postsForFriend) {
                if(!map.get("username").equals(map.get("dst_username")) && map.get("dst_username").equals(friend.getUsername())) {
                    posts.add(createPost(map));
                }
            }
        }
        catch (DAOException e) {
            System.out.println(e);
        }
        return posts;
    }

    public ArrayList<Post> getPostsReceived(User user) {
        // fetches posts on user's wall that were posted by friends, username = friend & dst_username = user

        ArrayList<Post> posts = new ArrayList<>();
        ArrayList<String> likes = new ArrayList<>();
        ArrayList<String> dislikes = new ArrayList<>();

        try {
            ArrayList<Map<String, String>> postsReceived = post_dao.getPostDest(user.getUsername());
            for (Map<String, String> map : postsReceived) {
                if (!map.get("username").equals(map.get("dst_username"))) {
                    posts.add(createPost(map));
                }
            }
        }
        catch (DAOException e) {
            System.out.println(e);
        }
        return posts;
    }

    public ArrayList<Post> getPostsOnFriend(User user, User friend) {
        // fetches posts on friends' walls that were posted by other users, username = ? & dst_username = friend, username can be = friend

        ArrayList<Post> posts = new ArrayList<>();

        try {
            ArrayList<Map<String, String>> postsOnFriend = post_dao.getPostDest(friend.getUsername());
            for (Map<String, String> map: postsOnFriend) {
                if (!map.get("username").equals(user.getUsername())) {
                    posts.add(createPost(map));
                }
            }
        }
        catch (DAOException e) {
            System.out.println(e);
        }
        return posts;
    }

    public static ArrayList<Reply> fetchRepliesFromDB(int pid) {
        //[{dt=2020-01-01 13, pid=7, rid=8, reply=nope, username=luke}]
        ArrayList<Map<String, String>> repliesForPost = new ArrayList<Map<String, String>>();
        ArrayList<Reply> replies = new ArrayList<>();
        try {
            repliesForPost = post_dao.getReplies(pid);
            for (Map<String, String> replyMap : repliesForPost) {
                Timestamp dt = M.toTimestamp(replyMap.get("dt"));
                replies.add(new Reply(replyMap.get("username"), replyMap.get("reply"), dt, Integer.parseInt(replyMap.get("rid")), Integer.parseInt(replyMap.get("pid"))));
            }
        }
        catch (DAOException e) {
            System.out.println("Unable to fetch replies.");
        }
        return replies;
    }

    public ArrayList<String> fetchLDFromDB(int pid, int ld) {
        // [{isLike=true, pid=11, username=jiemi}]
        ArrayList<Map<String, String>> likesForPost = new ArrayList<Map<String, String>>();
        ArrayList<String> likes = new ArrayList<>();
        ArrayList<String> dislikes = new ArrayList<>();
        try {
            likesForPost = post_dao.getLikes(pid);
            for (Map<String, String> likesMap : likesForPost) {
                if (Boolean.parseBoolean(likesMap.get("isLike"))) {
                    likes.add(likesMap.get("username"));
                } else {
                    dislikes.add(likesMap.get("username"));
                }
            }
        }
        catch (DAOException e) {
            System.out.println("Unable to fetch likes.");
        }

        if (ld == 0) {
            return dislikes;
        }
        return likes;
    }

    public static ArrayList<Gift> fetchGiftsFromDB(String forUser) {
        ArrayList<Gift> gifts = new ArrayList<>();
        try {
            ArrayList<Map<String, String>> giftsForUser = post_dao.getGifts(forUser);
            for (Map<String, String> giftMap : giftsForUser) {
                // {dt=2020-48-16 04-48-34, gid=1, gifter=jon, accepted=false, giftee=jiemi, crop=Papaya}
                
                int gid = Integer.parseInt(giftMap.get("gid"));
                boolean accepted = Boolean.parseBoolean(giftMap.get("accepted"));
                Timestamp dt = M.toTimestamp(giftMap.get("dt"));

                if (!accepted) {
                    Gift gift = new Gift(giftMap.get("gifter"), giftMap.get("giftee"), giftMap.get("crop"), accepted, dt.toInstant(), gid);
                    gifts.add(gift);
                }
            }
        }
        catch (DAOException e) {
            System.out.println("Unable to fetch gifts.");
        }
        return gifts;
    }

    public static ArrayList<Post> giftPosts(ArrayList<Gift> gifts) {
        ArrayList<Post> giftPosts = new ArrayList<>();
        for (Gift gift : gifts) {
            Post giftPost = new Post(gift.getGifter(), gift.getGiftee(), gift.getCrop(), gift.getGiftTime());
            giftPosts.add(giftPost);
        }
        return giftPosts;
    }

    public void updateGiftStatus(int gid) {
        try {
            ArrayList<Map<String, String>> updateGift = post_dao.setGifts(gid, true);
           // [{rows_updated=1}]
        }
        catch (DAOException e) {
            System.out.println("Unable to update gift status.");
        }
    }

    public void writePostToDB(String user, String dst_user, String text) {
        try {
            ArrayList<Map<String, String>> writePost = post_dao.setPosts(user, dst_user, text);
        }
        catch (DAOException e) {
            System.out.println("Unable to write post to DB.");
        }
    }

    public ArrayList<Map<String, String>> setLikesDislikes(int pid, String username, boolean isLike) {
        ArrayList<Map<String, String>> setLD = new ArrayList<Map<String, String>>();
        try {
            setLD = post_dao.setLike(pid, username, isLike);
        }
        catch (DAOException e) {
            System.out.println("Unable to process like/dislike.");
        }
        return setLD;
    }

    public ArrayList<Map<String, String>> removePost(int pid) {
        ArrayList<Map<String, String>> removePost = new ArrayList<Map<String, String>>();
        try {
            removePost = post_dao.deletePosts(pid);
        }
        catch (DAOException e) {
            System.out.println("Unable to remove post.");
        }
        return removePost;
    }

    public ArrayList<Map<String, String>> setReply(int pid, String username, String reply) {
        ArrayList<Map<String, String>> setReply = new ArrayList<Map<String, String>>();
        try {
            setReply = post_dao.setReplies(pid, username, reply);
        }
        catch (DAOException e) {
            System.out.println("Unable to reply.");
        }
        return setReply;
    }
}