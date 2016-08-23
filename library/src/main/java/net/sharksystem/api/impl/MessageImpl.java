package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSTSet;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.api.utils.ClassHelper;
import net.sharksystem.api.utils.SharkNetUtils;

import org.json.JSONException;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 03.08.16.
 */
public class MessageImpl implements Message {

    final static String MESSAGE_SENDER = "MESSAGE_SENDER";
    final static String MESSAGE_IS_SIGNED = "MESSAGE_IS_SIGNED";
    final static String MESSAGE_IS_ENCRYPTED = "MESSAGE_IS_SIGNED";
    final static String MESSAGE_IS_DISLIKED = "MESSAGE_IS_DISLIKED";
    final static String MESSAGE_IS_VERIFIED = "MESSAGE_IS_VERIFIED";
    final static String MESSAGE_IS_READ = "MESSAGE_IS_READ";
    final static String MESSAGE_IS_DIRECT_RECEIVED = "MESSAGE_IS_DIRECT_RECEIVED";

    private SharkNetEngine mEngine;
    private SharkKB mChatKB;
    private ASIPInformationSpace mInformationSpace;
    private Chat mChat;

    // Create a new Message from scratch -> I am the owner!
    MessageImpl(SharkNetEngine engine, Chat chat, SharkKB chatKB, ASIPSpace space) throws JSONException, SharkKBException {
        mEngine = engine;
        mChatKB = chatKB;
        mChat = chat;

        // There should not be an InformationSpace yet, so create one by adding the first information
        // by setting the sender
        PeerSemanticTag owner = mEngine.getMyProfile().getPST();
        String serializedOwner = ASIPSerializer.serializeTag(owner).toString();
        SharkNetUtils.setInfoWithName(mChatKB, space, MESSAGE_SENDER, serializedOwner);

        // now search for the desired InformationSpace and set it.
        Iterator<ASIPInformationSpace> informationSpaces = mChatKB.informationSpaces(space, true);
        if(informationSpaces.hasNext()){
            mInformationSpace = informationSpaces.next();
        }
    }

    MessageImpl(SharkNetEngine engine, Chat chat, SharkKB chatKB, ASIPInformationSpace informationSpace) throws SharkKBException {
        mEngine = engine;
        mChatKB = chatKB;
        mChat = chat;
        mInformationSpace = informationSpace;
    }

    @Override
    public Timestamp getTimestamp() throws SharkKBException {
        TimeSTSet times = mInformationSpace.getASIPSpace().getTimes();
        Iterator<TimeSemanticTag> timeSemanticTagIterator = times.tstTags();
        if(timeSemanticTagIterator.hasNext()) {
            TimeSemanticTag next = timeSemanticTagIterator.next();
            long from = next.getFrom();
            return new Timestamp(from);
        }
        return null;
    }

    @Override
    public Contact getSender() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_SENDER);
        if(information!=null){
            String informationContentAsString = information.getContentAsString();
            PeerSemanticTag sender = ASIPSerializer.deserializePeerTag(informationContentAsString);
            return mEngine.getContactByTag(sender);
        }
        return null;
    }

    @Override
    public List<Contact> getRecipients() throws SharkKBException {
        return mChat.getContacts();
    }

    @Override
    public void setContent(InputStream inputStream, String messageString, String mimeType) throws SharkKBException {
        ContentImpl content = new ContentImpl(mChatKB, mInformationSpace.getASIPSpace());
        content.setMimeType(mimeType);
        content.setInputStream(inputStream);
        content.setMessage(messageString);
    }

    @Override
    public Content getContent() throws SharkKBException {
        return new ContentImpl(mChatKB, mInformationSpace.getASIPSpace());
    }

    @Override
    public Chat getChat() {
        return mChat;
    }

    @Override
    public void deleteMessage() throws SharkKBException {
        // TODO removing Information by Space is correct?
        mChatKB.removeInformation(mInformationSpace.getASIPSpace());
    }

    @Override
    public void setSigned(boolean signed) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_SIGNED, signed);
    }

    @Override
    public boolean isSigned() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_SIGNED);
    }

    @Override
    public void setEncrypted(boolean encrypted) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_ENCRYPTED, encrypted);
    }

    @Override
    public boolean isEncrypted() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_ENCRYPTED);
    }

    @Override
    public void setDisliked(boolean isDisliked) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_DISLIKED, isDisliked);
    }

    @Override
    public boolean isDisliked() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_DISLIKED);
    }

    @Override
    public boolean isMine() throws SharkKBException {
        // TODO getOwner and myself
        return false;
    }

    @Override
    public void setVerified(boolean verified) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_VERIFIED, verified);
    }

    @Override
    public boolean isVerified() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_VERIFIED);
    }

    @Override
    public void setRead(boolean read) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_READ, read);
    }

    @Override
    public boolean isRead() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_READ);
    }

    @Override
    public void setDirectReceived(boolean directReceived) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_DIRECT_RECEIVED, directReceived);
    }

    @Override
    public boolean isDirectReceived() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mChatKB, mInformationSpace.getASIPSpace(), MESSAGE_IS_DIRECT_RECEIVED);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        try {
            return ClassHelper.equals(Message.class, this, o);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

}
