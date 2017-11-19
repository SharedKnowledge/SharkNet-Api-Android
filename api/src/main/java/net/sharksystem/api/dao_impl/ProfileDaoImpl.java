package net.sharksystem.api.dao_impl;

import android.content.Context;
import android.graphics.BitmapFactory;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.Interest;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoInterest;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_interfaces.ProfileDao;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Profile;
import net.sharksystem.api.utils.SharkNetUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dustin Feurich on 11.10.2017.
 */

public class ProfileDaoImpl implements ProfileDao {

    private SharkKB kb;
    private final static String ACTIVE_ENTRY_PROFILE = "ACTIVE_ENTRY_PROFILE";
    private final static String ACTIVE_OUT_PROFILE = "ACTIVE_OUT_PROFILE";

    ProfileDaoImpl(SharkKB kb) {
        this.kb = kb;
        Profile profile = get(null);
        if (profile == null) {
            profile = new Profile(kb.getOwner());
            profile.setActiveEntryInterest(generateInterest(ASIPSpace.DIRECTION_IN));
            profile.setActiveOutInterest(generateInterest(ASIPSpace.DIRECTION_OUT));
            add(profile);
        }
    }

    @Override
    public void add(Profile object) {
        try {
            if (object.getActiveEntryInterest() != null) {
                ASIPSpace asipSpace = this.kb.createASIPSpace(object.getActiveEntryInterest().getTopics(), object.getActiveEntryInterest().getTypes(),
                        object.getActiveEntryInterest().getApprovers(), null, null,
                        object.getActiveEntryInterest().getTimes(), object.getActiveEntryInterest().getLocations(), ASIPSpace.DIRECTION_IN);
                SharkNetUtils.setInfoWithName(kb, asipSpace, ACTIVE_ENTRY_PROFILE, "Entry Profile");
            }
            if (object.getActiveOutInterest() != null) {
                ASIPSpace asipSpace = this.kb.createASIPSpace(object.getActiveOutInterest().getTopics(), object.getActiveOutInterest().getTypes(),
                        object.getActiveOutInterest().getApprovers(), null, null,
                        object.getActiveOutInterest().getTimes(), object.getActiveOutInterest().getLocations(), ASIPSpace.DIRECTION_OUT);
                SharkNetUtils.setInfoWithName(kb, asipSpace, ACTIVE_OUT_PROFILE, "Out Profile");
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Profile> getAll() {

        List<Profile> profileList = new ArrayList<>();
        try {
            ASIPInterest asipInterest = InMemoSharkKB.createInMemoASIPInterest(null, null, null, null, null, null, null, ASIPSpace.DIRECTION_INOUT);
            Iterator<ASIPInformationSpace> allInformationSpaces = this.kb.getInformationSpaces(asipInterest);
            Profile profile = new Profile(kb.getOwner());
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                ASIPSpace asipSpace = next.getASIPSpace();
                ASIPInterest interest = new InMemoInterest(asipSpace.getTopics(), asipSpace.getTypes(), null,
                        asipSpace.getApprovers(), null, asipSpace.getTimes(), asipSpace.getLocations(), asipSpace.getDirection() );
                if (interest.getDirection() == ASIPSpace.DIRECTION_IN) {
                    profile.setActiveEntryInterest(interest);
                }
                else if (interest.getDirection() == ASIPSpace.DIRECTION_OUT) {
                    profile.setActiveOutInterest(interest);
                }
            }
            profileList.add(profile);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return profileList;
    }

    @Override
    public Profile get(PeerSemanticTag id) {
        return getAll().get(0);
    }

    @Override
    public void update(Profile object) {
        remove(object);
        add(object);
    }

    @Override
    public void remove(Profile object) {
        try {
            ASIPInterest asipInterest = InMemoSharkKB.createInMemoASIPInterest(null, null, null, null, null, null, null, ASIPSpace.DIRECTION_INOUT);
            Iterator<ASIPInformationSpace> allInformationSpaces = this.kb.getInformationSpaces(asipInterest);
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                ASIPSpace asipSpace = next.getASIPSpace();
                Profile profile = new Profile(kb.getOwner());
                ASIPInterest interest = new InMemoInterest(asipSpace.getTopics(), asipSpace.getTypes(), null,
                        asipSpace.getApprovers(), null, asipSpace.getTimes(), asipSpace.getLocations(), asipSpace.getDirection() );
                if (interest.getDirection() == ASIPSpace.DIRECTION_IN && profile.getActiveEntryInterest() != null) {
                    kb.removeInformation(asipSpace);
                }
                else if (interest.getDirection() == ASIPSpace.DIRECTION_OUT && profile.getActiveOutInterest() != null) {
                    kb.removeInformation(asipSpace);
                }
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {

        try {
            ASIPInterest asipInterest = InMemoSharkKB.createInMemoASIPInterest(null, null, null, null, null, null, null, ASIPSpace.DIRECTION_INOUT);
            Iterator<ASIPInformationSpace> spaces = this.kb.getInformationSpaces(asipInterest);
            int number = 0;
            while (spaces.hasNext()) {
                spaces.next();
                number++;
            }
            return number;
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private ASIPInterest generateInterest(int direction) {
        try {
            return InMemoSharkKB.createInMemoASIPInterest(null, null, null, null, null, null, null, direction);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return null;
    }

}
