package net.sharksystem.api.dao_interfaces;

import android.app.Activity;
import android.content.Context;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;

import java.util.List;

/**
 * Created by j4rvis on 4/7/17.
 */

public interface SharkNetApi {

    AndroidSharkEngine getSharkEngine();

    Contact getAccount();

    void setAccount(Contact contact);

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

    void pingMailServer(SemanticTag type, PeerSemanticTag receiver);

    void addRadarListener(NearbyPeerManager.NearbyPeerListener peerListener);

    void allowSyncInvitation(boolean allow);

    void startRadar();

    void stopRadar();

    NfcPkiPortEventListener initNFC(Activity activity);

    void startNFC();

    void stopNFC();

    void initPki();

}
