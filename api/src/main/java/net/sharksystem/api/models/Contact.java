package net.sharksystem.api.models;

import android.graphics.Bitmap;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;

/**
 * Created by j4rvis on 3/22/17.
 */

public class Contact {
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
}
