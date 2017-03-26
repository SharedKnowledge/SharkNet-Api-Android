package net.sharksystem.api.models;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;

/**
 * Created by j4rvis on 3/22/17.
 */

public class Contact implements Comparable<Contact>{
    private PeerSemanticTag tag;
    private String name;
    private String email;
    private Bitmap image;

    public Contact(String name, String email) {
        this.name = name;
        this.email = email;
        this.tag = InMemoSharkKB.createInMemoPeerSemanticTag(
                this.name,
                this.name + ":"  + this.email, // {name}:{email}
                "mail://" + this.email); // mail://{email}
    }

    public Contact(PeerSemanticTag tag) {
        this.tag = tag;
        if(name==null){
            this.name = tag.getName();
        }
        if(email==null){
            this.email = tag.getAddresses()[0];
        }
    }

    public PeerSemanticTag getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

        Contact contact = (Contact) o;

        if (!SharkCSAlgebra.identical(getTag(), contact.getTag())) return false;
        if (!getName().equals(contact.getName())) return false;
        return getEmail().equals(contact.getEmail());

    }

    @Override
    public int hashCode() {
        int result = getTag().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }

    @Override
    public int compareTo(@NonNull Contact o) {
        return getName().compareTo(o.getName());
    }
}
