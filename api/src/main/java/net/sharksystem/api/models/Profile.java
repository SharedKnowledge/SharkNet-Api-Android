package net.sharksystem.api.models;

import net.sharkfw.knowledgeBase.Interest;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bosch on 11.10.2017.
 */

public class Profile {

    public final static String PROFILE_ID = "PROFILE_ID";

    private SemanticTag id;
    private Contact owner;
    private List<Interest> entryInterests;
    private List<Interest> outInterests;
    private Interest activeEntryInterest;
    private Interest activeOutInterest;

    public Profile(Contact owner){
        this.id = InMemoSharkKB.createInMemoSemanticTag(PROFILE_ID,owner.getTag().getName() + new Date(System.currentTimeMillis()));
        entryInterests = new ArrayList<>();
        outInterests = new ArrayList<>();
    }

    public void setOwner(Contact owner) {
        this.owner = owner;
    }

    public void addEntryInterest(Interest interest) {
        this.entryInterests.add(interest);
    }

    public void addOutInterest(Interest interest) {
        this.outInterests.add(interest);
    }

    public void setActiveEntryInterest(Interest activeEntryInterest) {
        this.activeEntryInterest = activeEntryInterest;
    }

    public void setActiveOutInterest(Interest activeOutInterest) {
        this.activeOutInterest = activeOutInterest;
    }

    public Contact getOwner() {
        return owner;
    }

    public List<Interest> getEntryInterests() {
        return entryInterests;
    }

    public List<Interest> getOutInterests() {
        return outInterests;
    }

    public Interest getActiveEntryInterest() {
        return activeEntryInterest;
    }

    public Interest getActiveOutInterest() {
        return activeOutInterest;
    }

    public SemanticTag getId() {

        return id;
    }

    public void setId(SemanticTag id) {
        this.id = id;
    }






}
