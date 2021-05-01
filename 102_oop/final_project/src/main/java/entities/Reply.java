package entities;

import java.time.Instant;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Reply {
    private String user;
    private String reply;
    private Post post;
    private Instant replyTime;
    private int rid;
    private int pid;

    public Reply() {

    }

    // fetch existing reply from db
    public Reply(String user, String reply, Timestamp dt, int rid, int pid) {
        this.user = user;
        this.post = post;
        this.reply = reply;
        this.replyTime = dt.toInstant();
        this.rid = rid;
        this.pid = pid;
    }

    // create new reply
    public Reply(String user, Post post, String reply) {
        this.user = user;
        this.post = post;
        this.reply = reply;
        this.replyTime = Instant.now();
    }

    public String toString() {
        return this.user + ": " + this.reply;
    }

    public Instant getReplyTime() {
        return this.replyTime;
    }
}