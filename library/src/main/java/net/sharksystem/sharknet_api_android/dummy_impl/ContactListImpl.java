package net.sharksystem.sharknet_api_android.dummy_impl;

import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.ContactList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msc on 30.06.16.
 */
public class ContactListImpl implements ContactList {

    private List<Contact> contacts;

    public ContactListImpl() {
        this.contacts = new ArrayList<>();
    }

    @Override
    public void addContact(Contact contact) {
        if(contact!=null && !this.contacts.contains(contact)){
            this.contacts.add(contact);
        }
    }

    @Override
    public void addContact(PeerSemanticTag tag) {

    }

    @Override
    public void removeContact(Contact contact) {
        if(contact!=null && this.contacts.contains(contact)){
            this.contacts.remove(contact);
        }
    }

    @Override
    public void removeContact(PeerSemanticTag tag) {

    }

    @Override
    public List<Contact> getContacts() {
        return this.contacts;
    }

    @Override
    public PeerSTSet getContactsAsPSTSet() throws SharkKBException {
        PeerSTSet set = InMemoSharkKB.createInMemoPeerSTSet();

        for (Contact contact : this.contacts){
            PeerSemanticTag tag = contact.getPST();
            set.merge(tag);
        }

        return set;
    }
}
