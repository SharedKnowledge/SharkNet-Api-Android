package net.sharksystem.api.models;

import android.graphics.Bitmap;
import android.hardware.camera2.params.Face;
import android.support.annotation.NonNull;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.SpatialSemanticTag;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;

import java.util.Date;

/**
 * Created by j4rvis on 3/22/17.
 */

public class Message implements Comparable<Message> {

    public final static String MESSAGE_ID = "MESSAGE_ID";

    private SemanticTag id;
    private String content;
    private Bitmap imageContent;
    private Date date;
    private Contact sender;

    // Semantic Annotations for the message
    private SemanticTag topic = null;
    private SemanticTag type = null;
    private PeerSemanticTag peer = null;
    private TimeSemanticTag time = null;
    private SpatialSemanticTag location = null;

    private boolean isVerified = false;
    private boolean isSigned = false;
    private boolean isEncrypted = false;

    public Message(Contact sender) {
        this.sender = sender;
        this.date = new Date(System.currentTimeMillis());
        this.id = InMemoSharkKB.createInMemoSemanticTag(MESSAGE_ID,sender.getTag().getName() + date.getTime());
        //this.time = InMemoSharkKB.createInMemoTimeSemanticTag(date.getTime(), 0);
    }

    public Message(SemanticTag id, Date date, Contact sender) {
        this.id = id;
        this.date = date;
        this.sender = sender;
    }

    public Message(SemanticTag id, Date date, Contact sender, SemanticTag topic, SemanticTag type, PeerSemanticTag peer, TimeSemanticTag time, SpatialSemanticTag location) {
        this.id = id;
        this.date = date;
        this.sender = sender;
        this.topic = topic;
        this.type = type;
        this.peer = peer;
        this.time = time;
        this.location = location;
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

    public boolean isEmpty(){
        return content.isEmpty() && imageContent==null;
    }

    public void setSender(Contact sender) {
        this.sender = sender;
    }

    public SemanticTag getTopic() {
        return topic;
    }

    public void setTopic(SemanticTag topic) {
        this.topic = topic;
    }

    public SemanticTag getType() {
        return type;
    }

    public void setType(SemanticTag type) {
        this.type = type;
    }

    public PeerSemanticTag getPeer() {
        return peer;
    }

    public void setPeer(PeerSemanticTag peer) {
        this.peer = peer;
    }

    public TimeSemanticTag getTime() {
        return time;
    }

    public void setTime(TimeSemanticTag time) {
        this.time = time;
    }

    public SpatialSemanticTag getLocation() {
        return location;
    }

    public void setLocation(SpatialSemanticTag location) {
        this.location = location;
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

    @Override
    public int compareTo(@NonNull Message o) {
        return getDate().compareTo(o.getDate());
    }
}
