package net.sharksystem.api.dao_impl;

import android.content.Context;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 3/26/17.
 */

public class SharkNetApi {

    private static SharkNetApi mInstance = new SharkNetApi();
    private ChatDao mChatDao;
    private ContactDao mContactDao;
    private SharkKB mRootKb = new InMemoSharkKB();
    private AndroidSharkEngine mEngine;
    private Contact mAccount;

    private SharkNetApi() {
        try {
            mContactDao = new ContactDao(new InMemoSharkKB(InMemoSharkKB.createInMemoSemanticNet(), InMemoSharkKB.createInMemoSemanticNet(), mRootKb.getPeersAsTaxonomy(), InMemoSharkKB.createInMemoSpatialSTSet(), InMemoSharkKB.createInMemoTimeSTSet()));
            mChatDao = new ChatDao(mRootKb, mContactDao);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    public static SharkNetApi getInstance() {
        return mInstance;
    }

    public void initSharkEngine(Context context) {
        mEngine = new AndroidSharkEngine(context);
    }

    public AndroidSharkEngine getSharkEngine() {
        return mEngine;
    }

    // Account Methods

    public Contact getAccount() {
        return mAccount;
    }

    public void setAccount(Contact contact) {
        mContactDao.add(contact);
        mAccount = contact;
    }

    // DAO Methods

    public List<Chat> getChats() {
        return mChatDao.getAll();
    }

    public Chat getChat(SemanticTag tag) {
        return mChatDao.get(tag);
    }

    public void addChat(Chat chat) {
        mChatDao.add(chat);
    }

    public void updateChat(Chat chat) {
        mChatDao.update(chat);
    }

    public void removeChat(Chat chat) {
        mChatDao.remove(chat);
    }

    public int numberOfChats() {
        return mChatDao.size();
    }

    public List<Contact> getContacts() {
        return mContactDao.getAll();
    }

    public Contact getContact(PeerSemanticTag tag) {
        return mContactDao.get(tag);
    }

    public void addContact(Contact contact) {
        mContactDao.add(contact);
    }

    public void updateContact(Contact contact) {
        mContactDao.update(contact);
    }

    public void removeContact(Contact contact) {
        mContactDao.remove(contact);
    }

    public int numberOfContacts() {
        return mContactDao.size();
    }

    // Shark methods

    public void startRadar() {

    }

    public void stopRadar() {

    }

    public void initNFC() {

    }

    public void startNFC() {

    }

    public void stopNFC() {

    }


}
