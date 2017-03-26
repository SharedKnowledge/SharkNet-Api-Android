package net.sharksystem.api.models;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 3/22/17.
 */

public class Chat {
    private List<Message> messages = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();
    private Contact owner;
    private String title;
    private Bitmap image;

    public Chat(Contact owner, List<Contact> contactList) {
        this.owner = owner;
        this.contacts = contactList;
    }

    public Chat(Contact owner, Contact contact) {
        this.owner = owner;
        this.contacts.add(contact);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void addContact(Contact contact){
        if(!this.contacts.contains(contact)){
            this.contacts.add(contact);
        }
    }

    public void removeContact(Contact contact){
        this.contacts.remove(contact);
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }

    public Contact getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
