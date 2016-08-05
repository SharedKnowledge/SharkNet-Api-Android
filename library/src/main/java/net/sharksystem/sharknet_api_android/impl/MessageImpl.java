package net.sharksystem.sharknet_api_android.impl;

import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSTSet;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharksystem.sharknet_api_android.interfaces.Chat;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Message;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 03.08.16.
 */
public class MessageImpl implements Message {

    private final ASIPInformationSpace informationSpace;

    MessageImpl(ASIPInformationSpace informationSpace){
        this.informationSpace = informationSpace;
    }

    @Override
    public Timestamp getTimestamp() throws SharkKBException {
        TimeSTSet times = this.informationSpace.getASIPSpace().getTimes();
        Iterator<TimeSemanticTag> timeSemanticTagIterator = times.tstTags();
        if(timeSemanticTagIterator.hasNext()) {
            TimeSemanticTag next = timeSemanticTagIterator.next();
            long from = next.getFrom();
            return new Timestamp(from);
        }
        return null;
    }

    @Override
    public Contact getSender() {
        return null;
    }

    @Override
    public List<Contact> getRecipients() {
        return null;
    }

    @Override
    public Content getContent() {
        return null;
    }

    @Override
    public boolean isSigned() {
        return false;
    }

    @Override
    public boolean isEncrypted() {
        return false;
    }

    @Override
    public void deleteMessage() {

    }

    @Override
    public void setDisliked(boolean isDisliked) {

    }

    @Override
    public boolean isdisliked() {
        return false;
    }

    @Override
    public boolean isMine() {
        return false;
    }

    @Override
    public boolean isVerified() {
        return false;
    }

    @Override
    public void setVerified(boolean verified) {

    }

    @Override
    public Chat getChat() {
        return null;
    }

    @Override
    public boolean isRead() {
        return false;
    }

    @Override
    public void setRead(boolean read) {

    }

    @Override
    public void setSigned(boolean signed) {

    }

    @Override
    public void setEncrypted(boolean encrypted) {

    }

    @Override
    public boolean isDierectRecived() {
        return false;
    }

    @Override
    public void setDierectRecived(boolean dierectRecived) {

    }
}
