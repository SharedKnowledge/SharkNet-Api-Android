package net.sharksystem.api.impl;

import android.content.Context;

import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.kep.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Comment;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.api.interfaces.GetEvents;
import net.sharksystem.api.interfaces.Interest;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.api.interfaces.Profile;
import net.sharksystem.api.interfaces.RadarListener;
import net.sharksystem.api.interfaces.SharkNet;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeer;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.utils.SharkNetUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 01.08.16.
 */
public class SharkNetEngine implements SharkNet, NearbyPeerManager.NearbyPeerListener{

    private static final String ACTIVE_PROFILE = "ACTIVE_PROFILE";
    private static final String ACTIVE_PROFILE_PASSWORD = "ACTIVE_PROFILE_PASSWORD";

    public static final String SHARKNET_DOMAIN = "sharknet://";

    private static final SemanticTag mFeedType =
            InMemoSharkKB.createInMemoSemanticTag("FEED", "http://sharksystem.net/feed");

    private static final SemanticTag mSettingsType =
            InMemoSharkKB.createInMemoSemanticTag("SETTINGS", "http://sharksystem.net/settings");

    private AndroidSharkEngine mSharkEngine;

    private ASIPSpace mSettingsSpace = null;

    private ArrayList<RadarListener> mRadarListeners = new ArrayList<>();

    private static SharkNetEngine sInstance = null;

    // Shark
    private SharkKB mProfileKB = null;
    private SharkKB mRootKB = null;
    private SharkKB mContactKB = null;
    private SharkKB mFeedKB = null;
    private SharkKB mCommentKB = null;
    private List<SharkKB> mChatKBs = new ArrayList<>();
    private ArrayList<Contact> mContacts;
    private Context mContext;

    public static SharkNetEngine getSharkNet(){
        if (SharkNetEngine.sInstance == null) {
            sInstance = new SharkNetEngine();
        }
        return sInstance;
    }

    private SharkNetEngine(){
        mRootKB = new InMemoSharkKB();
        // Create shared KB
        try {
            mProfileKB = createKBFromRoot(mRootKB);
            mContactKB = createKBFromRoot(mRootKB);
            mFeedKB = createKBFromRoot(mRootKB);
            mCommentKB = createKBFromRoot(mRootKB);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    public void clearData() throws SharkKBException {
        mRootKB = new InMemoSharkKB();
        // Create shared KB
        mProfileKB = createKBFromRoot(mRootKB);
        mContactKB = createKBFromRoot(mRootKB);
        mFeedKB = createKBFromRoot(mRootKB);
        mCommentKB = createKBFromRoot(mRootKB);
        mChatKBs.clear();
    }

    public AndroidSharkEngine getSharkEngine() {
        return mSharkEngine;
    }

    public void setContext(Context context){
        mContext = context;
    }

    public void startShark() throws SharkKBException, SharkProtocolNotSupportedException, IOException {
        mSharkEngine = new AndroidSharkEngine(mContext);

        Profile profile = getMyProfile();
        mSharkEngine.setEngineOwnerPeer(profile.getPST());

        Interest interests = profile.getInterests();
        ASIPSpace asipSpace = null;
        if(interests!=null){
            asipSpace = interests.asASIPSpace();
        }
        mSharkEngine.setSpace(asipSpace);
        mSharkEngine.addNearbyPeerListener(this);
        mSharkEngine.startBluetooth();
        mSharkEngine.startDiscovery();
    }

    // Radar
    //
    //

    @Override
    public List<Contact> getRadarContacts() {
        return mContacts;
    }

    @Override
    public void addRadarListener(RadarListener listener) {
        if(!mRadarListeners.contains(listener)){
            mRadarListeners.add(listener);
        }
    }

    @Override
    public void removeRadarListener(RadarListener listener) {
        if(!mRadarListeners.contains(listener)){
            mRadarListeners.remove(listener);
        }
    }

    @Override
    public void onNearbyPeerFound(ArrayList<NearbyPeer> peers) {
        mContacts = new ArrayList<>();

        for( NearbyPeer peer : peers){
            try {
                Contact contact = newContact(peer.getSender());
                contact.setLastWifiContact(new Timestamp(peer.getLastSeen()));
                mContacts.add(contact);
                // TODO further information in this space?
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }
        for (RadarListener listener : mRadarListeners){
            listener.onNewRadarContact(mContacts);
        }
    }
    // Profiles
    //
    //

    @Override
    public List<Profile> getProfiles() throws SharkKBException {
        ArrayList<Profile> profiles = new ArrayList<>();

        Iterator<ASIPInformationSpace> asipInformationSpaceIterator = mProfileKB.informationSpaces();
        while (asipInformationSpaceIterator.hasNext()) {
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
    public boolean setActiveProfile(Profile myProfile, String password) throws SharkKBException, JSONException {
        myProfile.setPassword(password);
        PeerSemanticTag tag = myProfile.getPST();
        String string = ASIPSerializer.serializeTag(tag).toString();
        mSettingsSpace = SharkNetUtils.createCurrentTimeSpace(mProfileKB, mSettingsType);
        SharkNetUtils.setInfoWithName(mProfileKB, mSettingsSpace, ACTIVE_PROFILE, string);
        return true;
    }

    @Override
    public Profile getMyProfile() throws SharkKBException {
        String string = SharkNetUtils.getInfoAsString(mProfileKB, mSettingsSpace, ACTIVE_PROFILE);
        if(string != null && !string.isEmpty()){
            PeerSemanticTag tag = ASIPSerializer.deserializePeerTag(string);
            if(tag!=null){
                return getProfileByTag(tag);
            }
        }
        return null;
    }

    // Contacts
    //
    //

    @Override
    public List<Contact> getContacts() throws SharkKBException {
        List<Contact> contactList = new ArrayList<>();
        Iterator<ASIPInformationSpace> iterator = mContactKB.informationSpaces();
        while (iterator.hasNext()) {
            ASIPInformationSpace next = iterator.next();
            ContactImpl contact = new ContactImpl(mContactKB, next);
            contactList.add(contact);
        }
        return contactList;
    }

    @Override
    public Contact newContact(String nickname, String uid, String publicKey) throws SharkKBException {
        ContactImpl contact = new ContactImpl(mContactKB, nickname, uid);
        if (!publicKey.isEmpty()) {
            contact.setPublicKey(publicKey);
        }
        return contact;
    }

    @Override
    public Contact newContact(String nickName, String uId) throws SharkKBException {
        return newContact(nickName, uId, "");
    }

    @Override
    public Contact newContact(PeerSemanticTag tag) throws SharkKBException {
        return new ContactImpl(mContactKB, tag);
    }


    // Chats
    //
    //

    @Override
    public List<Chat> getChats() throws SharkKBException {
        Iterator<SharkKB> kbIterator = mChatKBs.iterator();
        ArrayList<Chat> chats = new ArrayList<>();
        while (kbIterator.hasNext()) {
            SharkKB next = kbIterator.next();
            chats.add(new ChatImpl(this, next));
        }
        return chats;
    }

    @Override
    public Chat newChat(List<Contact> recipients) throws SharkKBException, JSONException {
        InMemoSharkKB inMemoSharkKB = (InMemoSharkKB) createKBFromRoot(mRootKB);
        mChatKBs.add(inMemoSharkKB);

        return new ChatImpl(this, inMemoSharkKB, recipients, getMyProfile());
    }

    // Feeds
    //
    //

    private List<Feed> getFeeds() throws SharkKBException {
        List<Feed> feeds = new ArrayList<>();
        Iterator<ASIPInformationSpace> iterator = mFeedKB.informationSpaces();
        while (iterator.hasNext()) {
            ASIPInformationSpace next = iterator.next();
            if (next == null) {
                continue;
            }
            // checks if the infoSpace has an type SemanticTag with the SI from the mMessageType-Tag
            STSet types = next.getASIPSpace().getTypes();
            if (types.getSemanticTag(mFeedType.getSI()) != null) {
                Feed feed = new FeedImpl(this, mFeedKB, next);
                feeds.add(feed);
            }
        }
        return feeds;
    }

    @Override
    public List<Feed> getFeeds(boolean ascending) throws SharkKBException {
        return (List<Feed>) SharkNetUtils.sortList(getFeeds(), ascending);
    }

    @Override
    public List<Feed> getFeeds(int start_index, int stop_index, boolean ascending) throws SharkKBException {
        List<Feed> list = (List<Feed>) SharkNetUtils.cutList(getFeeds(), start_index, stop_index);
        return (List<Feed>) SharkNetUtils.sortList(list, ascending);
    }

    // TODO - getFeeds by Interest
    @Override
    public List<Feed> getFeeds(Interest i, int start_index, int stop_index, boolean ascending) {
        return null;
    }

    @Override
    public List<Feed> getFeeds(String search, int start_index, int stop_index, boolean ascending) throws SharkKBException {
        List<Feed> listTimestamp = (List<Feed>) SharkNetUtils.cutList(getFeeds(), start_index, stop_index);
        List<Feed> searched = (List<Feed>) SharkNetUtils.search(search, listTimestamp);
        return (List<Feed>) SharkNetUtils.sortList(searched, ascending);
    }

    @Override
    public List<Feed> getFeeds(Timestamp start, Timestamp end, int start_index, int stop_index, boolean ascending) throws SharkKBException {
        List<Feed> listTimestamp = (List<Feed>) SharkNetUtils.cutList(getFeeds(), start, end);
        List<Feed> listCuted = (List<Feed>) SharkNetUtils.cutList(listTimestamp, start_index, stop_index);
        return (List<Feed>) SharkNetUtils.sortList(listCuted, ascending);
    }

    //    TODO newFeed with Content
    @Override
    public Feed newFeed(Content content, Interest interest, Contact sender) throws SharkKBException, JSONException {
        return null;
    }

    @Override
    public Feed newFeed(InputStream stream, String mimeType, String message, Interest interest, Contact sender) throws SharkKBException, JSONException {
        ASIPSpace currentTimeSpace = SharkNetUtils.createCurrentTimeSpace(mFeedKB, mFeedType);
        Feed feed = new FeedImpl(this, mFeedKB, currentTimeSpace);
        feed.setContent(stream, message, mimeType);
        feed.setInterest(interest);
        feed.setSender(sender);
        return feed;
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

    public Feed getFeedById(String id) throws SharkKBException {
        List<Feed> list = getFeeds();
        Iterator<Feed> iterator = list.iterator();
        while (iterator.hasNext()) {
            Feed next = iterator.next();
            if (next.getId().equals(id)) {
                return next;
            }
        }
        return null;
    }

    public SharkKB getCommentsKB() {
        return mCommentKB;
    }

    public Contact getContactByTag(PeerSemanticTag tag) throws SharkKBException {
        return new ContactImpl(this.mContactKB, tag);
    }

    public Profile getProfileByTag(PeerSemanticTag tag) throws SharkKBException {
        return new ProfileImpl(this.mProfileKB, tag);
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
