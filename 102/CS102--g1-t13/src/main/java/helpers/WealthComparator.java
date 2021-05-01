package helpers;
import java.util.Comparator;
import entities.User;

public class WealthComparator implements Comparator<User>{
    public int compare(User user1, User user2) {
        Integer u1 = user1.getGold();
        Integer u2 = user2.getGold();
        return u2.compareTo(u1);
    } 
}