package net.sharksystem.sharknet_api_android.impl;

import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.sync.SyncKB;
import net.sharksystem.sharknet_api_android.interfaces.Chat;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Message;

import org.json.JSONException;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 01.08.16.
 */
public class ChatImpl implements Chat {

    public static final String SHARKNET_CHAT_RECIPIENTS = "SHARKNET_CHAT_RECIPIENTS";
    
    private SharkNetEngine mSharkNetEngine;
    private SyncKB mChatKB;
    private List<Contact> mRecipients;

    public ChatImpl(SharkNetEngine sharkNetEngine, SharkKB sharkKB) throws SharkKBException {
        mChatKB = new SyncKB(sharkKB);
        mSharkNetEngine = sharkNetEngine;

        String property = sharkKB.getProperty(SHARKNET_CHAT_RECIPIENTS);

        PeerSTSet stSet = ASIPSerializer.deserializePeerSTSet(null, property);

        Enumeration<PeerSemanticTag> enumeration = stSet.peerTags();
        while (enumeration.hasMoreElements()){
            PeerSemanticTag next = enumeration.nextElement();
            mRecipients.add(mSharkNetEngine.getContactByTag(next));
        }
    }

    public ChatImpl(SharkKB sharkKB, List<Contact> recipients) throws SharkKBException {

        mChatKB = new SyncKB(sharkKB);

        PeerSTSet peers = InMemoSharkKB.createInMemoPeerSTSet();

        Iterator<Contact> iterator = recipients.iterator();
        while (iterator.hasNext()){
            Contact next = iterator.next();
            peers.merge(next.getPST());
        }

        String serializedPeers = ""; // TODO ASIPSerializer.serializeSTSet(peers);

        mChatKB.setProperty(SHARKNET_CHAT_RECIPIENTS, serializedPeers);

        mRecipients = recipients;
    }

    private ASIPSpace createASIPSpace() throws SharkKBException {
        TimeSemanticTag timeSemanticTag =
                mChatKB.getTimeSTSet().createTimeSemanticTag(System.currentTimeMillis(), 0);
        return mChatKB.createASIPSpace(null, null, null, null, null, timeSemanticTag, null, ASIPSpace.DIRECTION_OUT);
    }

    @Override
    public void sendMessage(Content content) throws SharkKBException {
//        ASIPSpace space = createASIPSpace();
//        new MessageImpl(space);
//        mChatKB.addInformation(content.getInputStream(), content.getLength(), space);
    }

    @Override
    public void sendMessage(InputStream inputStream, String messageString, String mimeType) throws JSONException, SharkKBException {
        ASIPSpace space = createASIPSpace();
        MessageImpl message = new MessageImpl(mSharkNetEngine, mChatKB, space);
        message.setContent(inputStream, messageString, mimeType);
    }

    @Override
    public void delete() {

    }

    @Override
    public List<Message> getMessages(boolean descending) {
        return null;
    }

    @Override
    public List<Message> getMessages(int startIndex, int stopIndex, boolean descending) {
        return null;
    }

    @Override
    public List<Message> getMessages(Timestamp start, Timestamp stop, boolean descending) {
        return null;
    }

    @Override
    public List<Message> getMessages(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean descending) {
        return null;
    }

    @Override
    public List<Message> getMessages(String search, int startIndex, int stopIndex, boolean descending) {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public List<Contact> getContacts() {
        return null;
    }

    @Override
    public void setPicture(Content picture) {

    }

    @Override
    public Content getPicture() {
        return null;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public Contact getOwner() {
        return null;
    }

    @Override
    public Timestamp getTimestamp() {
        return null;
    }

    @Override
    public void addContact(List<Contact> cList) {

    }

    @Override
    public void removeContact(List<Contact> cList) {

    }

    @Override
    public void setAdmin(Contact admin) {

    }

    @Override
    public Contact getAdmin() {
        return null;
    }
}
