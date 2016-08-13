package net.sharksystem.sharknet_api_android.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.sync.SyncKB;
import net.sharksystem.sharknet_api_android.interfaces.Chat;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Message;
import net.sharksystem.sharknet_api_android.utils.SharkNetUtils;

import org.json.JSONException;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 01.08.16.
 */
public class ChatImpl implements Chat {

    // Chat Types - for the different asipSpaces

    private final SemanticTag mMessageType =
            InMemoSharkKB.createInMemoSemanticTag("MESSAGE", "http://sharksystem.net/message");

    private final SemanticTag mChatConfigurationType =
            InMemoSharkKB.createInMemoSemanticTag("CHAT_CONFIG", "http://sharksystem.net/chat_config");

    private ASIPSpace mChatConfigSpace = null;

    // Chat Config Information Names
    private final String CHAT_RECIPIENTS = "CHAT_RECIPIENTS";
    private final String CHAT_OWNER = "CHAT_OWNER";
    private final String CHAT_PICTURE = "CHAT_PICTURE";
    private final String CHAT_TITLE = "CHAT_TITLE";

    private SharkNetEngine mSharkNetEngine;
    private SyncKB mChatKB;
//    private List<Contact> mRecipients;

    // Called
    public ChatImpl(SharkNetEngine sharkNetEngine, SharkKB sharkKB) throws SharkKBException {
        mChatKB = new SyncKB(sharkKB);
        mSharkNetEngine = sharkNetEngine;

        mChatConfigSpace = mChatKB.createASIPSpace(null, mChatConfigurationType,
                null, null, null, null, null, ASIPSpace.DIRECTION_NOTHING);

//        String property = sharkKB.getProperty(CHAT_RECIPIENTS);
//
//        PeerSTSet stSet = ASIPSerializer.deserializePeerSTSet(null, property);
//
//        Enumeration<PeerSemanticTag> enumeration = stSet.peerTags();
//        while (enumeration.hasMoreElements()){
//            PeerSemanticTag next = enumeration.nextElement();
//            mRecipients.add(mSharkNetEngine.getContactByTag(next));
//        }
    }

    // Called to create a chat
    public ChatImpl(SharkKB sharkKB, List<Contact> recipients) throws SharkKBException, JSONException {

        mChatKB = new SyncKB(sharkKB);
        mChatConfigSpace = mChatKB.createASIPSpace(null, mChatConfigurationType,
                null, null, null, null, null, ASIPSpace.DIRECTION_NOTHING);

        setContacts(recipients);
    }

    private ASIPSpace createMessageSpace() throws SharkKBException {
        TimeSemanticTag timeSemanticTag =
                mChatKB.getTimeSTSet().createTimeSemanticTag(System.currentTimeMillis(), 0);
        return mChatKB.createASIPSpace(null, mMessageType, null, null, null, timeSemanticTag, null, ASIPSpace.DIRECTION_OUT);
    }

    @Override
    public void sendMessage(Content content) throws SharkKBException {
//        ASIPSpace space = createMessageSpace();
//        new MessageImpl(space);
//        mChatKB.addInformation(content.getInputStream(), content.getLength(), space);
    }

    @Override
    public void sendMessage(InputStream inputStream, String messageString, String mimeType) throws JSONException, SharkKBException {
        ASIPSpace space = createMessageSpace();
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

    private void setContacts(List<Contact> contacts) throws SharkKBException, JSONException {
        PeerSTSet peers = InMemoSharkKB.createInMemoPeerSTSet();

        Iterator<Contact> iterator = contacts.iterator();
        while (iterator.hasNext()){
            Contact next = iterator.next();
            peers.merge(next.getPST());
        }

        String serializedPeers = ASIPSerializer.serializeSTSet(peers).toString();

        ASIPInformation information = mChatKB.addInformation(serializedPeers, mChatConfigSpace);
        information.setName(CHAT_RECIPIENTS);
    }

    @Override
    public List<Contact> getContacts() throws SharkKBException {
        if(mSharkNetEngine==null){
            return null;
        }

        ArrayList<Contact> list = new ArrayList<>();

        Iterator<ASIPInformation> iterator = mChatKB.getInformation(mChatConfigSpace);
        while(iterator.hasNext()){
            ASIPInformation next = iterator.next();
            // Check if we have an information with the serialized PSTSet as content
            if(next.getName().equals(CHAT_RECIPIENTS)){
                String contentAsString = next.getContentAsString();
                // Deserialize the PSTSet and get the peerTags
                PeerSTSet peerSTSet = ASIPSerializer.deserializePeerSTSet(null, contentAsString);
                Enumeration<PeerSemanticTag> peerTags = peerSTSet.peerTags();
                while (peerTags.hasMoreElements()){
                    PeerSemanticTag peerSemanticTag = peerTags.nextElement();
                    // Get the contact for the PST from the sharkNEtEngine and add it to the list.
                    Contact contact = mSharkNetEngine.getContactByTag(peerSemanticTag);
                    list.add(contact);
                }
                return list;
            }
        }

        return null;
    }

    @Override
    public void addContact(Contact contact) throws SharkKBException, JSONException {
        List<Contact> contacts = getContacts();
        contacts.add(contact);
        setContacts(contacts);

    }

    @Override
    public void removeContact(Contact contact) throws SharkKBException, JSONException {
        List<Contact> contacts = getContacts();
        contacts.remove(contact);
        setContacts(contacts);
    }

    @Override
    public void setPicture(InputStream picture, String mimeType) throws SharkKBException {
        ContentImpl content = new ContentImpl(mChatKB, mChatConfigSpace);
        content.setInputStream(picture);
        content.setMimeType(mimeType);
    }

    @Override
    public Content getPicture() {
        return new ContentImpl(mChatKB, mChatConfigSpace);
    }

    @Override
    public void setTitle(String title) throws SharkKBException {
        if(title.isEmpty()){
            return;
        }
        ASIPInformation information = mChatKB.addInformation(title, mChatConfigSpace);
        information.setName(CHAT_TITLE);
    }

    @Override
    public String getTitle() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mChatKB, mChatConfigSpace, CHAT_TITLE);
        if (information!=null){
            return information.getContentAsString();
        }
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
    public void setAdmin(Contact admin) {

    }

    @Override
    public Contact getAdmin() {
        return null;
    }
}
