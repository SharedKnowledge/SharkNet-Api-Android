package net.sharksystem.sharknet_api_android.dummy_impl;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Interest;
import net.sharksystem.sharknet_api_android.interfaces.Profile;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by msc on 30.06.16.
 */
public class ContactImpl implements Contact {

    private PeerSemanticTag peerSemanticTag;

    private String name;
    private String email;
    private String uid;


    public ContactImpl(PeerSemanticTag peerSemanticTag) {
        this.peerSemanticTag = peerSemanticTag;
    }

    public ContactImpl(String name, String email) {
        this.name = name;
        this.email = email;

        this.uid = name + email + System.currentTimeMillis();

        this.peerSemanticTag = InMemoSharkKB.createInMemoPeerSemanticTag(this.name, this.uid, this.email);
    }

    @Override
    public PeerSemanticTag getPST() {
        return this.peerSemanticTag;
    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public void setNickname(String nickname) {

    }

    @Override
    public String getUID() {
        return null;
    }

    @Override
    public void setUID(String uid) {

    }

    @Override
    public Content getPicture() {
        return null;
    }

    @Override
    public void setPicture(Content pic) {

    }

    @Override
    public String getPublicKey() {
        return null;
    }

    @Override
    public void setPublicKey(String publicKey) {

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
    public void addName(String name) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void addTelephonnumber(String telephonnumber) {

    }

    @Override
    public List<String> getTelephonnumber() {
        return null;
    }

    @Override
    public void addNote(String note) {

    }

    @Override
    public String getNote() {
        return null;
    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public Timestamp getLastWifiContact() {
        return null;
    }

    @Override
    public void setLastWifiContact(Timestamp lastWifiContact) {

    }
}
