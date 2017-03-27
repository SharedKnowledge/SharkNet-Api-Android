package net.sharksystem.api.dao_impl;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.peer.SharkEngine;
import net.sharksystem.api.SharkApp;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;

import java.util.List;

/**
 * Created by j4rvis on 3/26/17.
 */

public class SharkNetApi {

    private final ChatDao mChatDao;
    private ContactDao mContactDao;
    private SharkKB mRootKb = new InMemoSharkKB();
    private AndroidSharkEngine mEngine;

    private static SharkNetApi mInstance = new SharkNetApi();
    private Contact mAccount;

    private SharkNetApi() {
        try {
            mContactDao = new ContactDao(
                    new InMemoSharkKB(
                            InMemoSharkKB.createInMemoSemanticNet(),
                            InMemoSharkKB.createInMemoSemanticNet(),
                            mRootKb.getPeersAsTaxonomy(),
                            InMemoSharkKB.createInMemoSpatialSTSet(),
                            InMemoSharkKB.createInMemoTimeSTSet()
                    )
            );
            mEngine = new AndroidSharkEngine(SharkApp.getContext());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        mChatDao = new ChatDao(mRootKb, mContactDao);
    }

    public static SharkNetApi getInstance(){
        return mInstance;
    }

    public AndroidSharkEngine getSharkEngine() {
        return mEngine;
    }

    // Account Methods

    public void setAccount(Contact contact){
        mContactDao.add(contact);
        mAccount = contact;
    }

    public Contact getAccount(){
        return mAccount;
    }

    // DAO Methods

    public List<Chat> getChats(){
        return mChatDao.getAll();
    }

    public Chat getChat(SemanticTag tag){
        return mChatDao.get(tag);
    }

    public void addChat(Chat chat){
        mChatDao.add(chat);
    }

    public void updateChat(Chat chat){
        mChatDao.update(chat);
    }

    public void removeChat(Chat chat){
        mChatDao.remove(chat);
    }

    public int numberOfChats(){
        return mChatDao.size();
    }

    public List<Contact> getContacts(){
        return mContactDao.getAll();
    }

    public Contact getContact(PeerSemanticTag tag){
        return mContactDao.get(tag);
    }

    public void addContact(Contact contact){
        mContactDao.add(contact);
    }

    public void updateContact(Contact contact){
        mContactDao.update(contact);
    }

    public void removeContact(Contact contact){
        mContactDao.remove(contact);
    }

    public int numberOfContacts(){
        return mContactDao.size();
    }

    // Shark methods

    public void initSharkEngine(){

    }

    public void startRadar(){

    }

    public void stopRadar(){

    }

    public void initNFC(){

    }

    public void startNFC(){

    }

    public void stopNFC(){

    }


}
