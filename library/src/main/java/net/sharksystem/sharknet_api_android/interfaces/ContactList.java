package net.sharksystem.sharknet_api_android.interfaces;

import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;

import java.util.List;

/**
 * Created by msc on 30.06.16.
 */
public interface ContactList {

    public void addContact(Contact contact);

    public void addContact(PeerSemanticTag tag);

    public void removeContact(Contact contact);

    public void removeContact(PeerSemanticTag tag);

    public List<Contact> getContacts();

    public PeerSTSet getContactsAsPSTSet() throws SharkKBException;

}
