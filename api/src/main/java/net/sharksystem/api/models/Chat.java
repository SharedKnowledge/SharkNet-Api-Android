package net.sharksystem.api.models;

import android.graphics.Bitmap;

import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by j4rvis on 3/22/17.
 */

public class Chat {

    public final static String CHAT_ID = "CHAT_ID";

    private SemanticTag id;
    private List<Message> messages = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();
    private Contact owner;
    private String title;
    private Bitmap image;

    public Chat(Contact owner, List<Contact> contactList) {
        this.owner = owner;
        if(contactList!=null){
            this.contacts = contactList;
        }
        this.id = InMemoSharkKB.createInMemoSemanticTag(CHAT_ID, this.contacts.size() + this.owner.getName() + System.currentTimeMillis());
    }

    public Chat(Contact owner, List<Contact> contactList, SemanticTag id) {
        this.owner = owner;
        if(contactList!=null){
            this.contacts = contactList;
        }
        this.id = id;
    }

    public Chat(Contact owner, Contact contact) {
        this.owner = owner;
        this.contacts.add(contact);
        this.id = InMemoSharkKB.createInMemoSemanticTag(CHAT_ID, this.contacts.size() + owner.getName() + System.currentTimeMillis());
    }

    public SemanticTag getId() {
        return id;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (getId() != null ? !SharkCSAlgebra.identical(getId(), chat.getId()) : chat.getId() != null) return false;
        if (getMessages() != null ? !equalLists(getMessages(), chat.getMessages()) : chat.getMessages() != null)
            return false;
        if (getContacts() != null ? !equalLists(getContacts(), chat.getContacts()) : chat.getContacts() != null)
            return false;
        if (getOwner() != null ? !getOwner().equals(chat.getOwner()) : chat.getOwner() != null)
            return false;
        if (getTitle() != null ? !getTitle().equals(chat.getTitle()) : chat.getTitle() != null)
            return false;
        return getImage() != null ? getImage().equals(chat.getImage()) : chat.getImage() == null;

    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (getMessages() != null ? getMessages().hashCode() : 0);
        result = 31 * result + (getContacts() != null ? getContacts().hashCode() : 0);
        result = 31 * result + (getOwner() != null ? getOwner().hashCode() : 0);
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getImage() != null ? getImage().hashCode() : 0);
        return result;
    }

    public static boolean equalLists(List one, List two){
        if (one == null && two == null){
            return true;
        }

        if((one == null && two != null)
                || one != null && two == null
                || one.size() != two.size()){
            return false;
        }

        //to avoid messing the order of the lists we will use a copy
        //as noted in comments by A. R. S.
        one = new ArrayList<>(one);
        two = new ArrayList<>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }
}
