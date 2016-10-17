package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Blacklist;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Profile;
import net.sharksystem.api.interfaces.Setting;
import net.sharksystem.api.utils.ClassHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by j4rvis on 01.08.16.
 */
public class ProfileImpl extends ContactImpl implements Profile {

    public ProfileImpl(SharkKB contactKB, PeerSemanticTag tag) throws SharkKBException {
        super(contactKB, tag);
    }

    public ProfileImpl(SharkKB sharkKB, String nickname, String deviceId) throws SharkKBException {
        super(sharkKB, nickname, deviceId);
    }

    public ProfileImpl(SharkKB sharkKB, ASIPInformationSpace informationSpace) throws SharkKBException {
        super(sharkKB, informationSpace);
    }

    @Override
    public Setting getSettings() {
        return new SettingImpl(mSharkKB, mSpace);
    }

    @Override
    public void save() {
    }

    @Override
    public boolean login(String password) {
        return false;
    }

    @Override
    public void setPassword(String password) {
    }

    @Override
    public boolean isEqual(Profile p) {
        return false;
    }

    @Override
    public Blacklist getBlacklist() {
        return null;
    }

    @Override
    public void renewKeys() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        try {
            return ClassHelper.equals(Profile.class, this, o);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
