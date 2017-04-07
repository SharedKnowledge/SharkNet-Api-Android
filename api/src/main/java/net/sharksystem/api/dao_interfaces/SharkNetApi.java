package net.sharksystem.api.dao_interfaces;

import android.app.Activity;
import android.content.Context;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;

import java.util.List;

/**
 * Created by j4rvis on 4/7/17.
 */

public interface SharkNetApi {

    void initSharkEngine(Context context);

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

    void startRadar();

    void stopRadar();

    void initNFC(Activity activity);

    void startNFC();

    void stopNFC();

}
