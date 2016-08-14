package net.sharksystem.sharknet_api_android.impl;

import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.sharknet_api_android.interfaces.Chat;
import net.sharksystem.sharknet_api_android.interfaces.Comment;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Feed;
import net.sharksystem.sharknet_api_android.interfaces.GetEvents;
import net.sharksystem.sharknet_api_android.interfaces.Interest;
import net.sharksystem.sharknet_api_android.interfaces.Message;
import net.sharksystem.sharknet_api_android.interfaces.Profile;
import net.sharksystem.sharknet_api_android.interfaces.SharkNet;

import org.json.JSONException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 01.08.16.
 */
public class SharkNetEngine implements SharkNet {

    public static final String SHARKNET_DOMAIN = "sharkknet://";

    private static SharkNetEngine sInstance =  null;

    // Shark
    private SharkKB mProfileKB = null;
    private SharkKB mRootKB = null;
    private SharkKB mContactKB = null;
    private List<SharkKB> mChatKBs = new ArrayList<>();

    public static SharkNet getSharkNet() throws SharkKBException {
        if(SharkNetEngine.sInstance == null){
            sInstance = new SharkNetEngine();
        }
        return sInstance;
    }

    private SharkNetEngine() throws SharkKBException {
        mRootKB = new InMemoSharkKB();
        // Create shared KB
        mProfileKB = createKBFromRoot(mRootKB);
        mContactKB = createKBFromRoot(mRootKB);
    }

    // Profiles
    //
    //

    @Override
    public List<Profile> getProfiles() throws SharkKBException {
        ArrayList<Profile> profiles = new ArrayList<>();

        Iterator<ASIPInformationSpace> asipInformationSpaceIterator = mProfileKB.informationSpaces();
        while (asipInformationSpaceIterator.hasNext()){
            ASIPInformationSpace next = asipInformationSpaceIterator.next();
            ProfileImpl profile = new ProfileImpl(mProfileKB, next);
            profiles.add(profile);
        }
        return profiles;
    }

    @Override
    public Profile newProfile(String nickname, String deviceID) throws SharkKBException {
        return new ProfileImpl(mProfileKB, nickname, deviceID);
    }


    // TODO setActiveProfile
    @Override
    public boolean setActiveProfile(Profile myProfile, String password) {
        return false;
    }

//    TODO getMyProfile
    @Override
    public Profile getMyProfile() {
        return null;
    }

    // Contacts
    //
    //

    @Override
    public List<Contact> getContacts() throws SharkKBException {
        List<Contact> contactList = new ArrayList<>();
        Iterator<ASIPInformationSpace> iterator = mContactKB.informationSpaces();
        while (iterator.hasNext()){
            ASIPInformationSpace next = iterator.next();
            ContactImpl contact = new ContactImpl(mContactKB, next);
            contactList.add(contact);
        }
        return contactList;
    }

    @Override
    public Contact newContact(String nickname, String uid, String publicKey) throws SharkKBException {

        ContactImpl contact = new ContactImpl(mContactKB, nickname, uid);
        if(!publicKey.isEmpty()){
            contact.setPublicKey(publicKey);
        }
        return contact;
    }

    @Override
    public Contact newContact(String nickName, String uId) throws SharkKBException {
        return newContact(nickName, uId, "");
    }

    // Chats
    //
    //

    @Override
    public List<Chat> getChats() throws SharkKBException {
        Iterator<SharkKB> kbIterator = mChatKBs.iterator();
        ArrayList<Chat> chats = new ArrayList<>();
        while (kbIterator.hasNext()){
            SharkKB next = kbIterator.next();
            chats.add(new ChatImpl(this, next));
        }
        return chats;
    }

    @Override
    public Chat newChat(List<Contact> recipients) throws SharkKBException, JSONException {
        InMemoSharkKB inMemoSharkKB = (InMemoSharkKB) createKBFromRoot(mRootKB);
        mChatKBs.add(inMemoSharkKB);

        return new ChatImpl(inMemoSharkKB, recipients, getMyProfile());
    }

    // Feeds
    //
    //

//    TODO getFeeds
    @Override
    public List<Feed> getFeeds(boolean descending) {
        return null;
    }

    @Override
    public List<Feed> getFeeds(int start_index, int stop_index, boolean descending) {
        return null;
    }

    @Override
    public List<Feed> getFeeds(Interest i, int start_index, int stop_index, boolean descending) {
        return null;
    }

    @Override
    public List<Feed> getFeeds(String search, int start_index, int stop_index, boolean descending) {
        return null;
    }

    @Override
    public List<Feed> getFeeds(Timestamp start, Timestamp end, int start_index, int stop_index, boolean descending) {
        return null;
    }

//    TODO newFeed
    @Override
    public Feed newFeed(Content content, Interest interest, Contact sender) {
        return null;
    }

    // Misc
    //
    //

//  TODO exchangeContactNFC
    @Override
    public void exchangeContactNFC() {

    }

//    TODO addListener
    @Override
    public void addListener(Profile p, GetEvents listener) {

    }

//    TODO inform
    @Override
    public void removeListener(Profile p, GetEvents listener) {

    }

    @Override
    public void informMessage(Message m) {

    }

    @Override
    public void informFeed(Feed f) {

    }

    @Override
    public void informComment(Comment c) {

    }

    @Override
    public void informContact(Contact c) {

    }

    // Facade Getter

    public Contact getContactByTag(PeerSemanticTag tag) throws SharkKBException {
        return new ContactImpl(this.mContactKB, tag);
    }

    private SharkKB createKBFromRoot(SharkKB sharkKB) throws SharkKBException {
        return new InMemoSharkKB(
            sharkKB.getTopicsAsSemanticNet(),
            sharkKB.getTypesAsSemanticNet(),
            sharkKB.getPeersAsTaxonomy(),
            sharkKB.getSpatialSTSet(),
            sharkKB.getTimeSTSet()
        );
    }
}
