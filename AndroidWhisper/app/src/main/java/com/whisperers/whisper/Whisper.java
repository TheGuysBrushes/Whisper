package com.whisperers.whisper;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 * Created by Work on 1/30/2017.
 */

public class Whisper implements Serializable{
    private Date time;
    private String content;
    private boolean has_been_send_by_me;
    private boolean completed;
    private int id;

    public Whisper(String message, boolean sent_by_host) {
        content= message;
        has_been_send_by_me= sent_by_host;
        completed = false;
    }

    public Whisper(String message) {
        content= message;
        has_been_send_by_me= new Random().nextBoolean();
        completed = false;
    }

    public Date getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public boolean hasBeenSendByMe() {
        return has_been_send_by_me;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
