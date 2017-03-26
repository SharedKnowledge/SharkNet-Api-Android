package net.sharksystem.api.models;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by j4rvis on 3/22/17.
 */

public class Message {
    private String content;
    private Bitmap imageContent;
    private Date date;
    private Contact sender;
    private boolean isVerified;
    private boolean isSigned;
    private boolean isEncrypted;
    private boolean isMine;

    public Message(Contact sender) {
        this.sender = sender;
        this.date = new Date(System.currentTimeMillis());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getImageContent() {
        return imageContent;
    }

    public void setImageContent(Bitmap imageContent) {
        this.imageContent = imageContent;
    }

    public Date getDate() {
        return date;
    }

    // TODO Just for testing purpose
    public void setDate(Date date) {
        this.date = date;
    }

    public Contact getSender() {
        return sender;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        isEncrypted = encrypted;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isEmpty(){
        return content.isEmpty() && imageContent==null;
    }
}
