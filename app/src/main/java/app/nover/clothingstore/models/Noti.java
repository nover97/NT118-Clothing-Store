package app.nover.clothingstore.models;

import java.util.Date;

public class Noti {
    public String title;
    public String content;
    public Date time;
    public boolean onclicked;
    public String documentId; // Trường lưu trữ ID của tài liệu

    public Noti() {}

    public Noti(String title, String content, Date time, boolean onclicked) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.onclicked = onclicked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isOnclicked() {
        return onclicked;
    }

    public void setOnclicked(boolean onclicked) {
        this.onclicked = onclicked;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}