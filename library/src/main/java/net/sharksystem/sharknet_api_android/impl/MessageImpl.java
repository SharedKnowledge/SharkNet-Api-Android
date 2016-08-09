package net.sharksystem.sharknet_api_android.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSTSet;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharksystem.sharknet_api_android.dummy_impl.*;
import net.sharksystem.sharknet_api_android.interfaces.Chat;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Message;
import net.sharksystem.sharknet_api_android.utils.SharkNetUtils;

import org.json.JSONException;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 03.08.16.
 */
public class MessageImpl implements Message {

    final static String MESSAGE_SENDER = "MESSAGE_SENDER";
    final static String MESSAGE_RECIPIENTS = "MESSAGE_RECIPIENTS";
    final static String MESSAGE_IS_SIGNED = "MESSAGE_IS_SIGNED";
    final static String MESSAGE_IS_ENCRYPTED = "MESSAGE_IS_SIGNED";
    final static String MESSAGE_IS_DISLIKED = "MESSAGE_IS_DISLIKED";
    final static String MESSAGE_IS_VERIFIED = "MESSAGE_IS_VERIFIED";
    final static String MESSAGE_IS_READ = "MESSAGE_IS_READ";
    final static String MESSAGE_IS_DIRECT_RECEIVED = "MESSAGE_IS_DIRECT_RECEIVED";

    private SharkNetEngine mEngine;
    private SharkKB mChatKB;
    private ASIPInformationSpace mInformationSpace;

    // Create a new Message from scratch -> I am the owner!
    MessageImpl(SharkNetEngine engine, SharkKB chatKB, ASIPSpace space) throws JSONException, SharkKBException {
        mEngine = engine;
        mChatKB = chatKB;

        // There should not be an InformationSpace yet, so create one by adding the first information
        // by setting the sender
        PeerSemanticTag owner = mChatKB.getOwner();
        String serializedOwner = ASIPSerializer.serializeTag(owner).toString();
        ASIPInformation senderInformation = mChatKB.addInformation(serializedOwner, space);
        senderInformation.setName(MESSAGE_SENDER);

        // now search for the desired InformationSpace and set it.
        Iterator<ASIPInformationSpace> informationSpaces = mChatKB.informationSpaces(space, true);
        if(informationSpaces.hasNext()){
            mInformationSpace = informationSpaces.next();
        }
    }

    MessageImpl(SharkNetEngine engine, SharkKB chatKB, ASIPInformationSpace informationSpace) throws SharkKBException {
        mEngine = engine;
        mChatKB = chatKB;
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
    public List<Contact> getRecipients() {
        return null;
    }

    @Override
    public Content getContent() {
        return null;
    }

    @Override
    public void setContent(InputStream inputStream, String messageString, String mimeType) throws SharkKBException {
        ContentImpl content = new ContentImpl(mChatKB, mInformationSpace.getASIPSpace());
        content.setMimeType(mimeType);
        content.setInputStream(inputStream);
        content.setMessage(messageString);
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
}
