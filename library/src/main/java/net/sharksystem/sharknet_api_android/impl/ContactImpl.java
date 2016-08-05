package net.sharksystem.sharknet_api_android.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Interest;
import net.sharksystem.sharknet_api_android.interfaces.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 01.08.16.
 */
public class ContactImpl implements Contact {

    private SharkKB sharkKB;
    private ASIPSpace asipSpace;
    public final static String INFONAME_NAME = "INFONAME_NAME";

    public final static String INFONAME_NICKNAME = "INFONAME_NICKNAME";
    public final static String INFONAME_UID = "INFONAME_UID";
    public final static String INFONAME_PICTURE = "INFONAME_PICTURE";
    public final static String INFONAME_PUBLIC_KEY = "INFONAME_PUBLIC_KEY";
    public final static String INFONAME_INTEREST = "INFONAME_INTEREST";
    public final static String INFONAME_TELEPHONE = "INFONAME_TELEPHONE";
    public final static String INFONAME_EMAIL = "INFONAME_EMAIL";
    public final static String INFONAME_NOTE = "INFONAME_NOTE";

    public ContactImpl(SharkKB sharkKB, PeerSemanticTag tag) throws SharkKBException {

        this.sharkKB = sharkKB;
        this.asipSpace = createASIPSpace(tag);

        // SET FIRST INFO - NAME
        this.setInfoWithName(INFONAME_NICKNAME, tag.getName());
    }

    public ContactImpl(SharkKB sharkKB, String nickname, String deviceId) throws SharkKBException {
        this(sharkKB, ContactImpl.createPeerSemanticTag(nickname, deviceId));
    }

    public ContactImpl(SharkKB sharkKB, ASIPInformationSpace informationSpace) throws SharkKBException {
        this.sharkKB = sharkKB;
        this.asipSpace = informationSpace.getASIPSpace();
    }

    public static PeerSemanticTag createPeerSemanticTag(String nickname, String deviceId) {
        String si = SharkNetEngine.SHARKNET_DOMAIN + deviceId + "." + System.currentTimeMillis();
        return InMemoSharkKB.createInMemoPeerSemanticTag(nickname, si, null);
    }

    private ASIPSpace createASIPSpace(PeerSemanticTag tag) throws SharkKBException {
        return this.sharkKB.createASIPSpace((SemanticTag) null, null, null, tag, null, null, null, ASIPSpace.DIRECTION_NOTHING);
    }

    private ASIPInformation getInfoByName(String name) throws SharkKBException {
        Iterator<ASIPInformation> information = this.sharkKB.getInformation(this.asipSpace);
        while (information.hasNext()) {
            ASIPInformation next = information.next();
            if (next.getName().equals(name)) {
                return next;
            }
        }
        return null;
    }

    private void setInfoWithName(String name, String content) throws SharkKBException {
        ASIPInformation asipInformation = this.sharkKB.addInformation(content, this.asipSpace);
        asipInformation.setName(name);
    }


    private void setInfoWithName(String name, InputStream content) throws SharkKBException, IOException {
        ASIPInformation asipInformation = this.sharkKB.addInformation(content, content.available(), this.asipSpace);
        asipInformation.setName(name);
    }


    @Override
    public PeerSemanticTag getPST() throws SharkKBException {
        return this.asipSpace.getSender();
    }

    @Override
    public String getNickname() throws SharkKBException {
        ASIPInformation information = getInfoByName(INFONAME_NICKNAME);
        return information != null ? information.getContentAsString() : null;
    }

    @Override
    public void setNickname(String nickname) throws SharkKBException {
        this.setInfoWithName(INFONAME_NICKNAME, nickname);
    }

    @Override
    public String getUID() throws SharkKBException {
        ASIPInformation information = getInfoByName(INFONAME_UID);
        return information != null ? information.getContentAsString() : null;
    }

    @Override
    public void setUID(String uid) throws SharkKBException {
        this.setInfoWithName(INFONAME_NICKNAME, uid);
    }

    @Override
    public Content getPicture() throws SharkKBException {
//        return getInfoByName(INFONAME_PICTURE); TODO IMPLEMENT CONTENT
        return null;
    }

    @Override
    public void setPicture(Content pic) throws IOException, SharkKBException {
        this.setInfoWithName(INFONAME_PICTURE, pic.getInputstream());
    }

    @Override
    public String getPublicKey() throws SharkKBException {
        ASIPInformation information = getInfoByName(INFONAME_PUBLIC_KEY);
        return information != null ? information.getContentAsString() : null;
    }

    @Override
    public void setPublicKey(String publicKey) throws SharkKBException {
        this.setInfoWithName(INFONAME_PUBLIC_KEY, publicKey);
    }

    @Override
    public Timestamp getPublicKeyExpiration() {
        return null;
    }

    @Override
    public String getPublicKeyFingerprint() {
        return null;
    }

    @Override
    public void deleteKey() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public Interest getInterests() {
        return null;
    }

    @Override
    public boolean isEqual(Contact c) {
        return false;
    }

    @Override
    public Profile getOwner() {
        return null;
    }

    @Override
    public void addName(String name) throws SharkKBException {
        this.setInfoWithName(INFONAME_NAME, name);
    }

    @Override
    public String getName() throws SharkKBException {
        ASIPInformation information = getInfoByName(INFONAME_NAME);
        return information != null ? information.getContentAsString() : null;
    }


    // SEPERATED BY ';'
    @Override
    public void addTelephonnumber(String telephonnumber) throws SharkKBException {
        if (getTelephonnumber().size() <= 0) {
            setInfoWithName(INFONAME_TELEPHONE, telephonnumber);
        } else {
            ASIPInformation information = getInfoByName(INFONAME_TELEPHONE);
            if (information != null) {
                String content = information.getContentAsString();
                information.setContent(content + ";" + telephonnumber);
            }
        }
    }

    @Override
    public List<String> getTelephonnumber() throws SharkKBException {
        ASIPInformation information = getInfoByName(INFONAME_TELEPHONE);
        if (information != null) {
            String contentAsString = information.getContentAsString();
            String[] split = contentAsString.split(";");
            return Arrays.asList(split);
        }
        return null;
    }

    @Override
    public void addNote(String note) throws SharkKBException {
        this.setInfoWithName(INFONAME_NOTE, note);
    }

    @Override
    public String getNote() throws SharkKBException {
        ASIPInformation information = getInfoByName(INFONAME_NOTE);
        return information != null ? information.getContentAsString() : null;
    }

    @Override
    public void setEmail(String email) throws SharkKBException {
        this.setInfoWithName(INFONAME_EMAIL, email);
    }

    @Override
    public String getEmail() throws SharkKBException {
        ASIPInformation information = getInfoByName(INFONAME_EMAIL);
        return information != null ? information.getContentAsString() : null;
    }

    @Override
    public Timestamp getLastWifiContact() {
        return null;
    }

    @Override
    public void setLastWifiContact(Timestamp lastWifiContact) {

    }
}
