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
    private SharkKB profileKB = null;
    private SharkKB rootKB = null;
    private SharkKB contactKB = null;

    public static SharkNet getSharkNet() throws SharkKBException {
        if(SharkNetEngine.sInstance == null){
            sInstance = new SharkNetEngine();
        }
        return sInstance;
    }

    private SharkNetEngine() throws SharkKBException {
        this.rootKB = new InMemoSharkKB();
        // Create shared KB
        this.profileKB = createKBFromRoot(this.rootKB);
    }

    @Override
    public List<Profile> getProfiles() throws SharkKBException {
        ArrayList<Profile> profiles = new ArrayList<>();

        Iterator<ASIPInformationSpace> asipInformationSpaceIterator = this.profileKB.informationSpaces();
        while (asipInformationSpaceIterator.hasNext()){
            ASIPInformationSpace next = asipInformationSpaceIterator.next();
            ProfileImpl profile = new ProfileImpl(this.profileKB, next);
            profiles.add(profile);
        }
        return profiles;
    }

    @Override
    public Profile newProfile(String nickname, String deviceID) {

        return null;
    }

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

    @Override
    public List<Contact> getContacts() {
        return null;
    }

    // Chat

    private List<SharkKB> chatKBs = new ArrayList<>();

    protected List<SharkKB> getChatKBs(){
        return this.chatKBs;
    }

    @Override
    public List<Chat> getChats() throws SharkKBException {
        Iterator<SharkKB> kbIterator = getChatKBs().iterator();
        ArrayList<Chat> chats = new ArrayList<>();
        while (kbIterator.hasNext()){
            SharkKB next = kbIterator.next();
            chats.add(new ChatImpl(this, next));
        }
        return chats;
    }

    @Override
    public Chat newChat(List<Contact> recipients) throws SharkKBException {
        InMemoSharkKB inMemoSharkKB = (InMemoSharkKB) createKBFromRoot(this.rootKB);
        this.chatKBs.add(inMemoSharkKB);

        return new ChatImpl(inMemoSharkKB, recipients);
    }

    //

    @Override
    public Feed newFeed(Content content, Interest interest, Contact sender) {
        return null;
    }

    @Override
    public Contact newContact(String nickname, String uid, String publickey) {
        return null;
    }

    @Override
    public boolean setProfile(Profile myProfile, String password) {
        return false;
    }

    @Override
    public Profile getMyProfile() {
        return null;
    }

    @Override
    public void exchangeContactNFC() {

    }

    @Override
    public void addListener(Profile p, GetEvents listener) {

    }

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

    // Fassade Getter

    public Contact getContactByTag(PeerSemanticTag tag) throws SharkKBException {
        return new ContactImpl(this.contactKB, tag);
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
