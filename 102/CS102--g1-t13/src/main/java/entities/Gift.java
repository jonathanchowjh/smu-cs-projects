package entities;

import java.time.Instant;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Gift {
    private String gifter;
    private String giftee;
    private String crop;
    private boolean accepted;
    private Instant giftTime;
    private int gid;

    public Gift(String gifter, String giftee, String crop, boolean accepted, Instant dt, int gid) {
        this.gifter = gifter;
        this.giftee = giftee;
        this.crop = crop;
        this.accepted = accepted;
        this.giftTime = dt; 
        this.gid = gid;
    }

    public String getGifter() {
        return this.gifter;
    }

    public String getGiftee() {
        return this.giftee;
    }

    public String getCrop() {
        return this.crop;
    }

    public Instant getGiftTime() {
        return this.giftTime;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public int getGID() {
        return this.gid;
    }
}