package com.example.demo.cms;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Content {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String title;
    private String summary;
    private String content;
    private String link;
    private boolean approved;

    // GETTERS
    public int getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getSummary() { return this.summary; }
    public String getContent() { return this.content; }
    public String getLink() { return this.link; }
    public boolean getApproved() { return this.approved; }

    // SETTERS
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setContent(String content) { this.content = content; }
    public void setLink(String link) { this.link = link; }
    public void setApproved(boolean approval) { this.approved = approval; }

    public String toString() {
        return title + " || " + summary + " || " + content + " || " + link + " || " + approved;
    }
}