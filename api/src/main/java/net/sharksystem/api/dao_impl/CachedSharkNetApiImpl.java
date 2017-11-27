package net.sharksystem.api.dao_impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.knowledgeBase.sync.manager.port.SyncMergeKP;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Broadcast;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;
import net.sharksystem.api.models.Profile;
import net.sharksystem.api.models.Settings;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 5/9/17.
 */

public class CachedSharkNetApiImpl implements SharkNetApi, SyncMergeKP.SyncMergeListener {

    private final SharkNetApiImpl mApi;

    private Contact mAccount = null;
    private boolean mAccountChanged = true;

    private List<Contact> mContacts = new ArrayList<>();
    private boolean mContactsChanged = true;

    private List<Chat> mChats = new ArrayList<>();
    private boolean mChatsChanged = true;

    public CachedSharkNetApiImpl(Context context) {
        mApi = new SharkNetApiImpl(context);
        mApi.getSharkEngine().getSyncManager().addSyncMergeListener(this);
    }

    @Override
    public AndroidSharkEngine getSharkEngine() {
        return mApi.getSharkEngine();
    }

    @Override
    public Contact getAccount() {
        if(mAccount ==null || mAccountChanged) {
            mAccount = mApi.getAccount();
            mAccountChanged = false;
        }
        return mAccount;
    }

    @Override
    public void setAccount(Contact contact) {
        mApi.setAccount(contact);
        mAccountChanged = true;
    }

    @Override
    public void setSettings(Settings settings) {
        mApi.setSettings(settings);
    }

    @Override
    public Settings getSettings() {
        return mApi.getSettings();
    }

    private void refreshChats(){
        if(mChats.isEmpty() || mChatsChanged) {
            mChats = mApi.getChats();
            mChatsChanged = false;
        }
    }

    @Override
    public List<Chat> getChats() {
        refreshChats();
        return mChats;
    }

    @Override
    public Chat getChat(SemanticTag tag) {
        refreshChats();
        for (Chat chat : mChats) {
            if(SharkCSAlgebra.identical(tag, chat.getId())) return chat;
        }
        return null;
    }

    @Override
    public void addChat(Chat chat) {
        mApi.addChat(chat);
        mChatsChanged = true;
    }

    @Override
    public void updateChat(Chat chat) {
        mApi.updateChat(chat);
        mChatsChanged = true;
    }

    @Override
    public void removeChat(Chat chat) {
        mApi.removeChat(chat);
        mChatsChanged = true;
    }

    @Override
    public int numberOfChats() {
        refreshChats();
        return mChats.size();
    }

    @Override
    public Broadcast getBroadcast() {
        return mApi.getBroadcast();
    }

    @Override
    public void updateBroadcast(Broadcast broadcast) {
        mApi.updateBroadcast(broadcast);
    }

    @Override
    public void updateBroadcast(Broadcast broadcast, Message message, List<PeerSemanticTag> peers) {
        mApi.updateBroadcast(broadcast, message, peers);
    }

    private void refreshContacts(){
        if(mContacts.isEmpty() || mContactsChanged) {
            mContacts = mApi.getContacts();
            mContactsChanged = false;
        }
    }

    @Override
    public List<Contact> getContacts() {
        refreshContacts();
        return mContacts;
    }

    @Override
    public Contact getContact(PeerSemanticTag tag) {
        refreshContacts();
        for (Contact contact : mContacts) {
            if(SharkCSAlgebra.identical(tag, contact.getTag())) return contact;
        }
        return null;
    }

    @Override
    public void addContact(Contact contact) {
        mApi.addContact(contact);
        mContactsChanged = true;
    }

    @Override
    public void updateContact(Contact contact) {
        mApi.updateContact(contact);
        mContactsChanged = true;
    }

    @Override
    public void removeContact(Contact contact) {
        mApi.removeContact(contact);
        mContactsChanged = true;
    }

    @Override
    public int numberOfContacts() {
        refreshContacts();
        return mContacts.size();
    }

    @Override
    public List<Profile> getProfiles() {
        return mApi.getProfiles();
    }

    @Override
    public Profile getProfile(PeerSemanticTag tag) {
        return mApi.getProfile(tag);
    }

    @Override
    public void addProfile(Profile profile) {
        mApi.addProfile(profile);
    }

    @Override
    public void updateProfile(Profile profile) {
        mApi.updateProfile(profile);
    }

    @Override
    public void removeProfile(Profile profile) {
        mApi.removeProfile(profile);
    }

    @Override
    public int numberOfProfiles() {
        return mApi.numberOfProfiles();
    }

    @Override
    public void pingMailServer(SemanticTag type, PeerSemanticTag receiver) {
        mApi.pingMailServer(type, receiver);
    }

    @Override
    public void addRadarListener(NearbyPeerManager.NearbyPeerListener peerListener) {
        mApi.addRadarListener(peerListener);
    }

    @Override
    public void allowSyncInvitation(boolean allow) {
        mApi.allowSyncInvitation(allow);
    }

    @Override
    public void startRadar() {
        mApi.startRadar();
    }

    @Override
    public void stopRadar() {
        mApi.stopRadar();
    }

    @Override
    public NfcPkiPortEventListener initNFC(Activity activity) {
        return mApi.initNFC(activity);
    }

    @Override
    public void startNFC() {
        mApi.startNFC();
    }

    @Override
    public void stopNFC() {
        mApi.stopNFC();
    }

    @Override
    public void initPki() {
        mApi.initPki();
    }

    @Override
    public void setNotificationResultActivity(Intent intent) {
        mApi.setNotificationResultActivity(intent);
    }

    @Override
    public void clearDbs() {
        mApi.clearDbs();
    }

    @Override
    public void onNewMerge(SyncComponent component, SharkKB changes) {
        mChatsChanged = true;
    }
}
