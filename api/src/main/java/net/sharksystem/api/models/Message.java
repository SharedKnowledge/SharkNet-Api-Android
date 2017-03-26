package net.sharksystem.api.models;

import android.graphics.Bitmap;
import android.hardware.camera2.params.Face;

import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;

import java.util.Date;

/**
 * Created by j4rvis on 3/22/17.
 */

public class Message {

    public final static String MESSAGE_ID = "MESSAGE_ID";

    private SemanticTag id;
    private String content;
    private Bitmap imageContent;
    private Date date;
    private Contact sender;
    private boolean isVerified = false;
    private boolean isSigned = false;
    private boolean isEncrypted = false;

    public Message(Contact sender) {
        this.sender = sender;
        this.date = new Date(System.currentTimeMillis());
        this.id = InMemoSharkKB.createInMemoSemanticTag(MESSAGE_ID,sender.getTag().getName() + date.getTime());
    }

    public Message(SemanticTag id, Date date, Contact sender) {
        this.id = id;
        this.date = date;
        this.sender = sender;
    }

    public SemanticTag getId() {
        return id;
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

    public boolean isEmpty(){
        return content.isEmpty() && imageContent==null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (isVerified() != message.isVerified()) return false;
        if (isSigned() != message.isSigned()) return false;
        if (isEncrypted() != message.isEncrypted()) return false;
        if (getId() != null ? !SharkCSAlgebra.identical(getId(),message.getId()) : message.getId() != null)
            return false;
        if (getContent() != null ? !getContent().equals(message.getContent()) : message.getContent() != null)
            return false;
        if (getImageContent() != null ? !getImageContent().equals(message.getImageContent()) : message.getImageContent() != null)
            return false;
        if (getDate() != null ? !getDate().equals(message.getDate()) : message.getDate() != null)
            return false;
        return getSender() != null ? getSender().equals(message.getSender()) : message.getSender() == null;

    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        result = 31 * result + (getImageContent() != null ? getImageContent().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getSender() != null ? getSender().hashCode() : 0);
        result = 31 * result + (isVerified() ? 1 : 0);
        result = 31 * result + (isSigned() ? 1 : 0);
        result = 31 * result + (isEncrypted() ? 1 : 0);
        return result;
    }
}
