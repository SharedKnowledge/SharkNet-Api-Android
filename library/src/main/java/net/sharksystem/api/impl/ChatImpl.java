package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.sync.SyncKB;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.api.utils.SharkNetUtils;

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

    // Chat Config Information Names
    public static final String CHAT_RECIPIENTS = "CHAT_RECIPIENTS";
    public static final String CHAT_OWNER = "CHAT_OWNER";
    public static final String CHAT_ADMIN = "CHAT_ADMIN";
    public static final String CHAT_TITLE = "CHAT_TITLE";
    public static final String CHAT_ID = "CHAT_ID";

    // Chat Types - for the different asipSpaces
    private final SemanticTag mMessageType =
            InMemoSharkKB.createInMemoSemanticTag("MESSAGE", "http://sharksystem.net/message");

    private final SemanticTag mChatConfigurationType =
            InMemoSharkKB.createInMemoSemanticTag("CHAT_CONFIG", "http://sharksystem.net/chat_config");

    private ASIPSpace mChatConfigSpace = null;

    // Shark
    private SharkNetEngine mSharkNetEngine;
    private SyncKB mChatKB;

    public ChatImpl(SharkNetEngine sharkNetEngine, SharkKB sharkKB) throws SharkKBException {
        mChatKB = new SyncKB(sharkKB);
        mSharkNetEngine = sharkNetEngine;

        mChatConfigSpace = mChatKB.createASIPSpace(null, mChatConfigurationType,
                null, null, null, null, null, ASIPSpace.DIRECTION_NOTHING);

        int size = mSharkNetEngine.getChats().size();
        long time = System.currentTimeMillis();

        SharkNetUtils.setInfoWithName(mChatKB, mChatConfigSpace, CHAT_ID, String.valueOf(size + time));
    }

    public ChatImpl(SharkNetEngine sharkNetEngine, SharkKB sharkKB, List<Contact> recipients, Contact owner) throws SharkKBException, JSONException {
        this(sharkNetEngine, sharkKB);

        setContacts(recipients);

        if(owner!=null){
            String serializedTag = ASIPSerializer.serializeTag(owner.getPST()).toString();
            ASIPInformation information = mChatKB.addInformation(serializedTag, mChatConfigSpace);
            information.setName(CHAT_OWNER);
        }
    }

    @Override
    public void sendMessage(Content content) throws SharkKBException {
//        ASIPSpace space = createMessageSpace();
//        new MessageImpl(space);
//        mChatKB.addInformation(content.getInputStream(), content.getLength(), space);
    }

    @Override
    public void sendMessage(InputStream inputStream, String messageString, String mimeType) throws JSONException, SharkKBException {
        ASIPSpace space = SharkNetUtils.createCurrentTimeSpace(mChatKB, mMessageType);
        MessageImpl message = new MessageImpl(mSharkNetEngine, this, mChatKB, space);
        message.setContent(inputStream, messageString, mimeType);
    }

    @Override
    public void sendMessage(InputStream inputStream, String messageString, String mimetype, Contact sender) throws JSONException, SharkKBException {
        ASIPSpace space = SharkNetUtils.createCurrentTimeSpace(mChatKB, mMessageType);
        MessageImpl message = new MessageImpl(mSharkNetEngine, this, mChatKB, space, sender);
        message.setContent(inputStream, messageString, mimetype);
    }

    @Override
    public void sendMessage(String message) throws JSONException, SharkKBException {
        this.sendMessage(null, message, null);
    }

    @Override
    public void sendMessage(String message, Contact sender) throws JSONException, SharkKBException {
        this.sendMessage(null, message, null, sender);
    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    private List<Message> getMessages() throws SharkKBException {
        ArrayList<Message> messages = new ArrayList<>();
        Iterator<ASIPInformationSpace> informationSpaces = mChatKB.informationSpaces();
        while (informationSpaces.hasNext()){
            ASIPInformationSpace next = informationSpaces.next();
            if(next == null){
                continue;
            }
            // checks if the infoSpace has an type SemanticTag with the SI from the mMessageType-Tag
            STSet types = next.getASIPSpace().getTypes();
            if(types.getSemanticTag(mMessageType.getSI())!=null){
                MessageImpl message = new MessageImpl(mSharkNetEngine, this, mChatKB, next);
                messages.add(message);
            }
        }
        return messages;
    }

    @Override
    public List<Message> getMessages(boolean ascending) throws SharkKBException {
        return (List<Message>) SharkNetUtils.sortList(getMessages(), ascending);
    }

    @Override
    public List<Message> getMessages(int startIndex, int stopIndex, boolean ascending) throws SharkKBException {
        List<Feed> list = (List<Feed>) SharkNetUtils.cutList(getMessages(), startIndex, stopIndex);
        return (List<Message>) SharkNetUtils.sortList(list, ascending);
    }

    @Override
    public List<Message> getMessages(Timestamp start, Timestamp stop, boolean ascending) throws SharkKBException {
        List<Feed> list = (List<Feed>) SharkNetUtils.cutList(getMessages(), start, stop);
        return (List<Message>) SharkNetUtils.sortList(list, ascending);
    }

    @Override
    public List<Message> getMessages(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean ascending) throws SharkKBException {
        List<Message> messages = getMessages(start, stop, ascending);
        return messages.subList(startIndex, stopIndex);
    }

    // Checks if the string is within the Message's content message :)
    @Override
    public List<Message> getMessages(String search, int startIndex, int stopIndex, boolean ascending) throws SharkKBException {
        List<Feed> containsContents = (List<Feed>) SharkNetUtils.search(search, getMessages());
        List<Feed> cutList = (List<Feed>) SharkNetUtils.cutList(containsContents, startIndex, stopIndex);
        return (List<Message>) SharkNetUtils.sortList(cutList, ascending);
    }

    private void setContacts(List<Contact> contacts) throws SharkKBException, JSONException {
        PeerSTSet peers = InMemoSharkKB.createInMemoPeerSTSet();

        Iterator<Contact> iterator = contacts.iterator();
        while (iterator.hasNext()){
            Contact next = iterator.next();
            peers.merge(next.getPST());
        }

        String serializedPeers = ASIPSerializer.serializeSTSet(peers).toString();

        SharkNetUtils.setInfoWithName(mChatKB, mChatConfigSpace, CHAT_RECIPIENTS, serializedPeers);
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
        SharkNetUtils.setInfoWithName(mChatKB, mChatConfigSpace, CHAT_TITLE, title);
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
    public Contact getOwner() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mChatKB, mChatConfigSpace, CHAT_OWNER);
        if(information!=null){
            String informationContentAsString = information.getContentAsString();
            PeerSemanticTag peerSemanticTag = ASIPSerializer.deserializePeerTag(informationContentAsString);
            return mSharkNetEngine.getContactByTag(peerSemanticTag);
        }
        return null;
    }

    @Override
    public void setAdmin(Contact admin) throws SharkKBException, JSONException {
        String serializedTag = ASIPSerializer.serializeTag(admin.getPST()).toString();
        SharkNetUtils.setInfoWithName(mChatKB, mChatConfigSpace, CHAT_ADMIN, serializedTag);
    }

    @Override
    public Contact getAdmin() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mChatKB, mChatConfigSpace, CHAT_ADMIN);
        if(information!=null){
            String informationContentAsString = information.getContentAsString();
            PeerSemanticTag peerSemanticTag = ASIPSerializer.deserializePeerTag(informationContentAsString);
            return mSharkNetEngine.getContactByTag(peerSemanticTag);
        }
        return null;
    }

    @Override
    public String getID() throws SharkKBException {
        return SharkNetUtils.getInfoAsString(mChatKB, mChatConfigSpace, CHAT_ID);
    }

    @Override
    public Timestamp getTimestamp() throws SharkKBException {
        List<Message> messages = getMessages(true);
        return messages.get(0).getTimestamp();
    }
}
