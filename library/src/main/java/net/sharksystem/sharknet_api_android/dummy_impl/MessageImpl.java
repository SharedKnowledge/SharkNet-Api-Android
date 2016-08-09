package net.sharksystem.sharknet_api_android.dummy_impl;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.sharknet_api_android.interfaces.Chat;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Message;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by msc on 30.06.16.
 */
public class MessageImpl implements Message {

    private InMemoSharkKB kb;

    private Contact sender;

    public MessageImpl(InMemoSharkKB kb, Contact sender, List<Contact> recipients) {
        this.kb = kb;
        this.sender = sender;
    }

    @Override
    public Timestamp getTimestamp() {
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
    public boolean isDisliked() {
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
    public boolean isDirectReceived() {
        return false;
    }

    @Override
    public void setDirectReceived(boolean dierectRecived) {

    }

    @Override
    public void setContent(InputStream inputStream, String messageString, String mimeType) throws SharkKBException {
        
    }
}
