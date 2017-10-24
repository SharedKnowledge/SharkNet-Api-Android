package net.sharksystem.api.dao_interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.sync.manager.port.SyncMergeKP;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Profile;
import net.sharksystem.api.models.Settings;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;

import java.util.List;

/**
 * Created by j4rvis on 4/7/17.
 */

public interface SharkNetApi extends SyncMergeKP.SyncMergeListener {

    AndroidSharkEngine getSharkEngine();

    Contact getAccount();

    void setAccount(Contact contact);

    void setSettings(Settings settings);

    Settings getSettings();

    List<Chat> getChats();

    Chat getChat(SemanticTag tag);

    void addChat(Chat chat);

    void updateChat(Chat chat);

    void removeChat(Chat chat);

    int numberOfChats();

    List<Contact> getContacts();

    Contact getContact(PeerSemanticTag tag);

    void addContact(Contact contact);

    void updateContact(Contact contact);

    void removeContact(Contact contact);

    int numberOfContacts();

    public List<Profile> getProfiles();

    public Profile getProfile(PeerSemanticTag tag);

    public void addProfile(Profile profile);

    public void updateProfile(Profile profile);

    public void removeProfile(Profile profile);

    public int numberOfProfiles();

    void pingMailServer(SemanticTag type, PeerSemanticTag receiver);

    void addRadarListener(NearbyPeerManager.NearbyPeerListener peerListener);

    void allowSyncInvitation(boolean allow);

    void startRadar();

    void stopRadar();

    NfcPkiPortEventListener initNFC(Activity activity);

    void startNFC();

    void stopNFC();

    void initPki();

    void setNotificationResultActivity(Intent intent);

    void clearDbs();
}
