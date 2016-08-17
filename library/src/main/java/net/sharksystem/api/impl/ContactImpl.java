package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.Interest;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.system.L;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Profile;
import net.sharksystem.api.utils.ClassHelper;
import net.sharksystem.api.utils.SharkNetUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * Created by j4rvis on 01.08.16.
 */
public class ContactImpl implements Contact {

    private SharkKB mSharkKB;
    private ASIPSpace mSpace;

    private final static String CONTACT_NAME = "CONTACT_NAME";
    private final static String CONTACT_NICKNAME = "CONTACT_NICKNAME";
    private final static String CONTACT_UID = "CONTACT_UID";
    private final static String CONTACT_PUBLIC_KEY = "CONTACT_PUBLIC_KEY";
    private final static String CONTACT_INTEREST = "CONTACT_INTEREST";
    private final static String CONTACT_TELEPHONE = "CONTACT_TELEPHONE";
    private final static String CONTACT_EMAIL = "CONTACT_EMAIL";
    private final static String CONTACT_NOTE = "CONTACT_NOTE";
    private final static String CONTACT_LAST_SEEN = "CONTACT_LAST_SEEN";

    public ContactImpl(SharkKB sharkKB, PeerSemanticTag tag) throws SharkKBException {
        mSharkKB = sharkKB;
        mSpace = createASIPSpace(tag);

        System.out.println(L.semanticTag2String(tag));
        System.out.println(L.asipSpace2String(mSpace));

        System.out.println("Name: " + tag.getName());
        System.out.println("SI: " + tag.getSI()[0]);
        System.out.println("PST-Name: " + mSpace.getSender().getName());

        // SET FIRST INFO - NAME
        setName(tag.getName());
    }

    public ContactImpl(SharkKB sharkKB, String nickname, String deviceId) throws SharkKBException {
        this(sharkKB, ContactImpl.createPeerSemanticTag(nickname, deviceId));
    }

    public ContactImpl(SharkKB sharkKB, ASIPInformationSpace informationSpace) throws SharkKBException {
        mSharkKB = sharkKB;
        mSpace = informationSpace.getASIPSpace();
    }

    public static PeerSemanticTag createPeerSemanticTag(String nickname, String deviceId) {
        String si = SharkNetEngine.SHARKNET_DOMAIN + deviceId + "." + System.currentTimeMillis();
        return InMemoSharkKB.createInMemoPeerSemanticTag(nickname, si, null);
    }

    private ASIPSpace createASIPSpace(PeerSemanticTag tag) throws SharkKBException {
        return mSharkKB.createASIPSpace((SemanticTag) null, null, null, tag, null, null, null, ASIPSpace.DIRECTION_NOTHING);
    }

//    private ASIPInformation getInfoByName(String name) throws SharkKBException {
//        Iterator<ASIPInformation> information = mSharkKB.getInformation(mSpace);
//        while (information.hasNext()) {
//            ASIPInformation next = information.next();
//            if (next.getName().equals(name)) {
//                return next;
//            }
//        }
//        return null;
//    }


    @Override
    public PeerSemanticTag getPST() throws SharkKBException {
        return mSpace.getSender();
    }

    @Override
    public String getNickname() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTACT_NICKNAME);
        return information != null ? information.getContentAsString() : null;
    }

    @Override
    public void setNickname(String nickname) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTACT_NICKNAME, nickname);
    }

    @Override
    public String getUID() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTACT_UID);
        return information != null ? information.getContentAsString() : null;
    }

    @Override
    public void setUID(String uid) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTACT_NICKNAME, uid);
    }

    @Override
    public Content getPicture() throws SharkKBException {
        return new ContentImpl(mSharkKB, mSpace);
    }

    @Override
    public void setPicture(InputStream is, String title, String mimeType) throws IOException, SharkKBException {
        ContentImpl content = new ContentImpl(mSharkKB, mSpace);
        content.setInputStream(is);
        content.setMessage(title);
        content.setMimeType(mimeType);
    }

    @Override
    public String getPublicKey() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTACT_PUBLIC_KEY);
        return information != null ? information.getContentAsString() : null;
    }

    @Override
    public void setPublicKey(String publicKey) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTACT_PUBLIC_KEY, publicKey);
    }

//    TODO getPublicKeyExpiration
    @Override
    public Timestamp getPublicKeyExpiration() {
        return null;
    }

//    TODO getPublicKeyFingerprint
    @Override
    public String getPublicKeyFingerprint() {
        return null;
    }

//    TODO deleteKey
    @Override
    public void deleteKey() {

    }

    @Override
    public void delete() throws SharkKBException {
        mSharkKB.removeInformation(mSpace);
    }

    @Override
    public void update() {

    }

    @Override
    public void setInterest(Interest interest) {

    }

    //    TODO getInterests
    @Override
    public Interest getInterests() {
        return null;
    }

//    TODO isEqual
    @Override
    public boolean isEqual(Contact c) {
        return false;
    }


//    TODO getOwner
    @Override
    public Profile getOwner() {
        return null;
    }

    @Override
    public void setName(String name) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTACT_NAME, name);
    }

    @Override
    public String getName() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTACT_NAME);
        return information != null ? information.getContentAsString() : null;
    }


    // SEPERATED BY ';'
    @Override
    public void addTelephoneNumber(String telephoneNumber) throws SharkKBException {
        if (getTelephoneNumber().size() <= 0) {
            SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTACT_TELEPHONE, telephoneNumber);
        } else {
            ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTACT_TELEPHONE);
            if (information != null) {
                String content = information.getContentAsString();
                information.setContent(content + ";" + telephoneNumber);
            }
        }
    }

    @Override
    public List<String> getTelephoneNumber() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTACT_TELEPHONE);
        if (information != null) {
            String contentAsString = information.getContentAsString();
            String[] split = contentAsString.split(";");
            return Arrays.asList(split);
        }
        return null;
    }

    @Override
    public void addNote(String note) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTACT_NOTE, note);
    }

    @Override
    public String getNote() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTACT_NOTE);
        return information != null ? information.getContentAsString() : null;
    }

    @Override
    public void setEmail(String email) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTACT_EMAIL, email);
    }

    @Override
    public String getEmail() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTACT_EMAIL);
        return information != null ? information.getContentAsString() : null;
    }

    @Override
    public Timestamp getLastWifiContact() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTACT_LAST_SEEN);
        if(information!=null){
            String string = information.getContentAsString();
            return new Timestamp(Long.getLong(string));
        }
        return null;
    }

    @Override
    public void setLastWifiContact(Timestamp lastWifiContact) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTACT_LAST_SEEN, String.valueOf(lastWifiContact.getTime()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        try {
            return ClassHelper.equals(Contact.class, this, o);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;

//        ContactImpl contact = (ContactImpl) o;
//
//        try {
//            if(getEmail()==null && contact.getEmail()==null){
//                return true;
//            } else if(getEmail()!=null && contact.getEmail()!=null){
//                if(!getEmail().equals(contact.getEmail())) return false;
//            } else return false;
//
//            if(getInterests()==null && contact.getInterests()==null) {
//                return true;
//            } else if(getInterests()!=null && contact.getInterests()!=null){
//                if(!SharkCSAlgebra.identical(getInterests(), contact.getInterests())) return false;
//            } else return false;
//
//            if(getLastWifiContact()==null && contact.getLastWifiContact()==null){
//                return true;
//            } else if(getLastWifiContact()!=null && contact.getLastWifiContact()!=null){
//                if(!getLastWifiContact().equals(contact.getLastWifiContact())) return false;
//            } else return false;
//
//            if(getName()==null && contact.getName()==null){
//                return true;
//            } else if(getName()!=null && contact.getName()!=null){
//                if(!getName().equals(contact.getName())) return false;
//            } else return false;
//
//            if(getNickname()==null && contact.getNickname()==null){
//                return true;
//            } else if(getNickname()!=null && contact.getNickname()!=null){
//                if(!getNickname().equals(contact.getNickname())) return false;
//            } else return false;
//
//            if(getNote()==null && contact.getNote()==null){
//                return true;
//            } else if(getNote()!=null && contact.getNote()!=null){
//                if(!getNote().equals(contact.getNote())) return false;
//            } else return false;
//
//            if(getOwner()==null && contact.getOwner()==null){
//                return true;
//            } else if(getOwner()!=null && contact.getOwner()!=null){
//                if(!getOwner().equals(contact.getOwner())) return false;
//            } else return false;
//
//            if(getPicture()==null && contact.getPicture()==null){
//                return true;
//            } else if(getPicture()!=null && contact.getPicture()!=null){
//                if(!getPicture().equals(contact.getPicture())) return false;
//            } else return false;
//
//            if(getPST()==null && contact.getPST()==null){
//                return true;
//            } else if(getPST()!=null && contact.getPST()!=null){
//                if(!SharkCSAlgebra.identical(getPST(), contact.getPST())) return false;
//            } else return false;
//
//            if(getPublicKey()==null && contact.getPublicKey()==null){
//                return true;
//            } else if(getPublicKey()!=null && contact.getPublicKey()!=null){
//                if(!getPublicKey().equals(contact.getPublicKey())) return false;
//            } else return false;
//
//            if(getPublicKeyExpiration()==null && contact.getPublicKeyExpiration()==null){
//                return true;
//            } else if(getPublicKeyExpiration()!=null && contact.getPublicKeyExpiration()!=null){
//                if(!getPublicKeyExpiration().equals(contact.getPublicKeyExpiration())) return false;
//            } else return false;
//
//            if(getPublicKeyFingerprint()==null && contact.getPublicKeyFingerprint()==null){
//                return true;
//            } else if(getPublicKeyFingerprint()!=null && contact.getPublicKeyFingerprint()!=null){
//                if(!getPublicKeyFingerprint().equals(contact.getPublicKeyFingerprint())) return false;
//            } else return false;
//
//            if(getTelephoneNumber()==null && contact.getTelephoneNumber()==null){
//                return true;
//            } else if(getTelephoneNumber()!=null && contact.getTelephoneNumber()!=null){
//                if(!getTelephoneNumber().equals(contact.getTelephoneNumber())) return false;
//            } else return false;
//
//            if(getUID()==null && contact.getUID()==null){
//                return true;
//            } else if(getUID()!=null && contact.getUID()!=null){
//                if(!getUID().equals(contact.getUID())) return false;
//            } else return false;
//
//            return true;
//        } catch (SharkKBException e) {
//            e.printStackTrace();
//        }
//        return false;
    }

//    TODO Implement hashCode, also for PST and Interest.
    @Override
    public int hashCode() {
        int result = 13;
        int hash = 0;

        try {
            hash += (getEmail() != null) ? getEmail().hashCode() : 0;
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        return mSpace != null ? mSpace.hashCode() : 0;
    }
}
