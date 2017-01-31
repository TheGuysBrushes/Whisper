package com.whisperers.whisper;

import java.util.Date;
import java.util.Random;

/**
 * Created by Work on 1/30/2017.
 */

public class Whisper {
    private Date time;
    private String content;
    private boolean has_been_send_by_me;

    public Whisper(String message, boolean sent_by_host) {
        content= message;
        has_been_send_by_me= sent_by_host;
    }

    public Whisper(String message) {
        content= message;
        has_been_send_by_me= new Random().nextBoolean();
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
}
