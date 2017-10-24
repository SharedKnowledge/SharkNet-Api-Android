package net.sharksystem.api.models;

import net.sharkfw.asip.ASIPInterest;
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
    private ASIPInterest activeEntryInterest;
    private ASIPInterest activeOutInterest;

    public Profile(Contact owner){
        this.id = InMemoSharkKB.createInMemoSemanticTag(PROFILE_ID,owner.getTag().getName() + new Date(System.currentTimeMillis()));
    }

    public void setOwner(Contact owner) {
        this.owner = owner;
    }

    public void setActiveEntryInterest(ASIPInterest activeEntryInterest) {
        this.activeEntryInterest = activeEntryInterest;
    }

    public void setActiveOutInterest(ASIPInterest activeOutInterest) {
        this.activeOutInterest = activeOutInterest;
    }

    public Contact getOwner() {
        return owner;
    }

    public ASIPInterest getActiveEntryInterest() {
        return activeEntryInterest;
    }

    public ASIPInterest getActiveOutInterest() {
        return activeOutInterest;
    }

    public SemanticTag getId() {

        return id;
    }

    public void setId(SemanticTag id) {
        this.id = id;
    }






}
