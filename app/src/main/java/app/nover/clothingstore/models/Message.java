package app.nover.clothingstore.models;

import java.util.Date;

public class Message {
    private String messageContent;
    private String who;
    private Date time;

    public Message() {
    }

    public Message(String messageContent, String who, Date time) {
        this.messageContent = messageContent;
        this.who = who;
        this.time = time;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
