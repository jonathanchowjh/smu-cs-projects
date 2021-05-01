package helpers;
import java.util.Comparator;
import entities.Post;

public class PostComparator implements Comparator<Post> {
    public int compare(Post p1, Post p2) {
        return p2.getPostTime().compareTo(p1.getPostTime());
    }
}