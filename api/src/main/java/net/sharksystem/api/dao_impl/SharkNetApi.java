package net.sharksystem.api.dao_impl;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;

import java.util.List;

/**
 * Created by j4rvis on 3/26/17.
 */

public class SharkNetApi {

    private final ChatDao chatDao;
    private ContactDao contactDao;
    private SharkKB root = new InMemoSharkKB();

    private static SharkNetApi mInstance = new SharkNetApi();

    private SharkNetApi() {
        try {
            contactDao = new ContactDao(createKBFromRoot());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        chatDao = new ChatDao(root, contactDao);
    }

    public static SharkNetApi getInstance(){
        return mInstance;
    }

    public List<Chat> getChats(){
        return chatDao.getAll();
    }

    public Chat getChat(SemanticTag tag){
        return chatDao.get(tag);
    }

    public void addChat(Chat chat){
        chatDao.add(chat);
    }

    public void updateChat(Chat chat){
        chatDao.update(chat);
    }

    public void removeChat(Chat chat){
        chatDao.remove(chat);
    }

    public List<Contact> getContacts(){
        return contactDao.getAll();
    }

    public Contact getContact(PeerSemanticTag tag){
        return contactDao.get(tag);
    }

    public void addContact(Contact contact){
        contactDao.add(contact);
    }

    public void updateContact(Contact contact){
        contactDao.update(contact);
    }

    public void removeContact(Contact contact){
        contactDao.remove(contact);
    }

    public int numberOfChats(){
        return chatDao.size();
    }

    public int numberOfContacts(){
        return contactDao.size();
    }

    private SharkKB createKBFromRoot() throws SharkKBException {
        return new InMemoSharkKB(InMemoSharkKB.createInMemoSemanticNet(), InMemoSharkKB.createInMemoSemanticNet(), root.getPeersAsTaxonomy(), InMemoSharkKB.createInMemoSpatialSTSet(), InMemoSharkKB.createInMemoTimeSTSet());
    }
}
