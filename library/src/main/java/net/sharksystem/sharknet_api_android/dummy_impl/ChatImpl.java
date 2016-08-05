package net.sharksystem.sharknet_api_android.dummy_impl;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.PeerTaxonomy;
import net.sharkfw.knowledgeBase.SemanticNet;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoGenericTagStorage;
import net.sharkfw.knowledgeBase.inmemory.InMemoSemanticNet;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.inmemory.InMemoSpatialSTSet;
import net.sharkfw.knowledgeBase.inmemory.InMemoTimeSTSet;
import net.sharksystem.sharknet_api_android.interfaces.Chat;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Message;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by msc on 30.06.16.
 */
public class ChatImpl implements Chat {

    private InMemoSharkKB inMemoSharkKB;
    private List<PeerSemanticTag> members;
    private PeerSemanticTag owner;

    public ChatImpl(PeerSemanticTag owner, List<PeerSemanticTag> members) {
        this.owner = owner;
        this.members = members;

        try {
            initializeKB();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }


    }

    private void initializeKB() throws SharkKBException {

        // PeerTaxonomy consisting of member PeerSemanticTags
        PeerTaxonomy inMemoPeerTaxonomy = InMemoSharkKB.createInMemoPeerTaxonomy();
        for (PeerSemanticTag tag : members){
            inMemoPeerTaxonomy.merge(tag);
        }

        // SemanticNet types
        SemanticNet types = new InMemoSemanticNet();

        // SpatialSTSet locations
        InMemoSpatialSTSet locations = new InMemoSpatialSTSet(new InMemoGenericTagStorage());

        // TimeSTSet
        InMemoTimeSTSet times = new InMemoTimeSTSet(new InMemoGenericTagStorage());

        this.inMemoSharkKB = new InMemoSharkKB(new InMemoSemanticNet(), types, inMemoPeerTaxonomy, locations, times );
        this.inMemoSharkKB.setOwner(this.owner);
    }

    private Message createMessage(Contact sender, List<Contact> receiver){
        return new MessageImpl(this.inMemoSharkKB, sender, receiver);
    }


    @Override
    public void sendMessage(Content content) {
        // create a message as infoSpace

        // add content as info to space
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
