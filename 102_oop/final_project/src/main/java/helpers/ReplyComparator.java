package helpers;
import java.util.Comparator;
import entities.Reply;

public class ReplyComparator implements Comparator<Reply> {
    public int compare(Reply r1, Reply r2) {
        return r2.getReplyTime().compareTo(r1.getReplyTime());
    }
}