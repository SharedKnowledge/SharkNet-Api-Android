package net.sharksystem.api.impl;

import android.app.Activity;
import android.content.Context;

import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.asip.serialization.ASIPMessageSerializerHelper;
import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.knowledgeBase.sync.manager.SyncManager.SyncInviteListener;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.api.interfaces.Interest;
import net.sharksystem.api.interfaces.Profile;
import net.sharksystem.api.interfaces.RadarListener;
import net.sharksystem.api.interfaces.SharkNet;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeer;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.shark.ports.NfcPkiPort;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;
import net.sharksystem.api.shark.ports.NfcPkiPortListener;
import net.sharksystem.api.utils.SharkNetUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 01.08.16.
 */
public class SharkNetEngine implements SharkNet, NearbyPeerManager.NearbyPeerListener, SyncInviteListener {

    public static final String SHARKNET_DOMAIN = "sharknet://";
    private static final String ACTIVE_PROFILE = "ACTIVE_PROFILE";
    private static final String ACTIVE_PROFILE_PASSWORD = "ACTIVE_PROFILE_PASSWORD";
    private static final SemanticTag mFeedType = InMemoSharkKB.createInMemoSemanticTag("FEED", "http://sharksystem.net/feed");
    private static final SemanticTag mSettingsType = InMemoSharkKB.createInMemoSemanticTag("SETTINGS", "http://sharksystem" + "" +
            ".net/settings");
    private static SharkNetEngine sInstance = null;
    private AndroidSharkEngine mSharkEngine;
    private ASIPSpace mSettingsSpace = null;
    private ArrayList<RadarListener> mRadarListeners = new ArrayList<>();
    private ArrayList<EventListener> mEventListeners = new ArrayList<>();
    // Shark
    private SharkKB mProfileKB = null;
    private SharkKB mRootKB = null;
    private SharkKB mContactKB = null;
    private SharkKB mFeedKB = null;
    private SharkKB mCommentKB = null;
    private List<SyncComponent> mChatComponents = new ArrayList<>();
    private ArrayList<Contact> mContacts;
    private Context mContext;

    private SharkNetEngine() {
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

    public static SharkNetEngine getSharkNet() {
        if (SharkNetEngine.sInstance == null) {
            sInstance = new SharkNetEngine();
        }
        return sInstance;
    }

    public void clearData() throws SharkKBException {
        mRootKB = new InMemoSharkKB();
        // Create shared KB
        mProfileKB = createKBFromRoot(mRootKB);
        mContactKB = createKBFromRoot(mRootKB);
        mFeedKB = createKBFromRoot(mRootKB);
        mCommentKB = createKBFromRoot(mRootKB);
        mChatComponents.clear();
    }

    public AndroidSharkEngine getSharkEngine() {
        return mSharkEngine;
    }

    public void setContext(Context context) {
        mContext = context;
        mSharkEngine = new AndroidSharkEngine(mContext, mRootKB);
    }

    public void startShark() throws SharkKBException, SharkProtocolNotSupportedException, IOException {

        Profile profile = getMyProfile();
        mSharkEngine.setEngineOwnerPeer(profile.getPST());

        Interest interests = profile.getInterests();
        ASIPSpace asipSpace = null;
        if (interests != null) {
            asipSpace = interests.asASIPSpace();
        }
        mSharkEngine.setSpace(asipSpace);

        // Sync
        mSharkEngine.getSyncManager().allowInvitation(true);
        mSharkEngine.getSyncManager().addInviteListener(this);

        // Discovery
        mSharkEngine.addNearbyPeerListener(this);
//        mSharkEngine.startBluetooth();
//        mSharkEngine.startDiscovery();

    }

    public void addEventListener(EventListener listener) {
        mEventListeners.add(listener);
    }

    // Listener
    //
    //

    @Override
    public List<Contact> getRadarContacts() {
        return mContacts;
    }


    // Radar
    //
    //

    @Override
    public void addRadarListener(RadarListener listener) {
        if (!mRadarListeners.contains(listener)) {
            mRadarListeners.add(listener);
        }
    }

    @Override
    public void removeRadarListener(RadarListener listener) {
        if (!mRadarListeners.contains(listener)) {
            mRadarListeners.remove(listener);
        }
    }

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
    public List<Feed> getFeeds(boolean ascending) throws SharkKBException {
        return (List<Feed>) SharkNetUtils.sortList(getFeeds(), ascending);
    }
    // Profiles
    //
    //

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
    public List<Chat> getChats() throws SharkKBException {
        Iterator<SyncComponent> componentIterator = mChatComponents.iterator();
        ArrayList<Chat> chats = new ArrayList<>();
        while (componentIterator.hasNext()) {
            SyncComponent next = componentIterator.next();
            chats.add(new ChatImpl(this, next));
        }
        return chats;
    }

    // Contacts
    //
    //

    @Override
    public Chat getChatById(String id) throws SharkKBException {
        for (Chat next : this.getChats()) {
            if (next.getID().equals(id)) {
                return next;
            }
        }
        return null;
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

    @Override
    public Profile newProfile(String nickname, String deviceID) throws SharkKBException {
        return new ProfileImpl(mProfileKB, nickname, deviceID);
    }

    @Override
    public Profile newProfile(PeerSemanticTag tag) throws SharkKBException {
        return new ProfileImpl(mProfileKB, tag);
    }


    // Chats
    //
    //

    @Override
    public Chat newChat(List<Contact> recipients) throws SharkKBException, JSONException {
        InMemoSharkKB inMemoSharkKB = (InMemoSharkKB) createKBFromRoot(mRootKB);
//        mChatKBs.add(inMemoSharkKB);

        PeerSTSet peerSTSet = InMemoSharkKB.createInMemoPeerSTSet();

        for (Contact contact : recipients) {
            peerSTSet.merge(contact.getPST());
        }

        SemanticTag uniqueChatId = createUniqueChatId(recipients);

        SyncComponent component = mSharkEngine.getSyncManager().createSyncComponent(inMemoSharkKB, uniqueChatId, peerSTSet, mSharkEngine.getOwner(), true);
        mChatComponents.add(component);

        return new ChatImpl(this, component, recipients, getMyProfile());
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

    // TODO setActiveProfile
    @Override
    public boolean setActiveProfile(Profile myProfile, String password) throws SharkKBException, JSONException {
        myProfile.setPassword(password);
        PeerSemanticTag tag = myProfile.getPST();
        String string = ASIPMessageSerializerHelper.serializeTag(tag).toString();
        mSettingsSpace = SharkNetUtils.createCurrentTimeSpace(mProfileKB, mSettingsType);
        SharkNetUtils.setInfoWithName(mProfileKB, mSettingsSpace, ACTIVE_PROFILE, string);
        return true;
    }

    // Feeds
    //
    //

    @Override
    public Profile getMyProfile() throws SharkKBException {
        String string = SharkNetUtils.getInfoAsString(mProfileKB, mSettingsSpace, ACTIVE_PROFILE);
        if (string != null && !string.isEmpty()) {
            PeerSemanticTag tag = ASIPMessageSerializerHelper.deserializePeerTag(string);
            if (tag != null) {
                return getProfileByTag(tag);
            }
        }
        return null;
    }

    /**
     * Creates the NfcPkiPort and sets the {{@link NfcPkiPortListener}} so
     * that the UI gets the
     * information about received messages etc. Also returns the
     * {{@link NfcPkiPortEventListener}}
     * so that the port can react on the decisions.
     *
     * @param listener
     * @return {{@link NfcPkiPortEventListener}} to set as listener
     */
    @Override
    public NfcPkiPortEventListener setupNfc(Activity activity, NfcPkiPortListener listener, ASIPKnowledge knowledge) throws SharkProtocolNotSupportedException, SharkNotSupportedException {
        // Create the nfcPort w/ the listener set
        NfcPkiPort nfcPkiPort = new NfcPkiPort(mSharkEngine, listener);
        mSharkEngine.setupNfc(activity, nfcPkiPort);
        mSharkEngine.stopNfc();
        mSharkEngine.offer(knowledge);
        return nfcPkiPort;
    }

    @Override
    public void startSendingViaNfc() throws SharkProtocolNotSupportedException, IOException {
        mSharkEngine.startNfc();
    }

    @Override
    public void onNearbyPeerFound(ArrayList<NearbyPeer> peers) {
        mContacts = new ArrayList<>();

        for (NearbyPeer peer : peers) {
            try {
                // TODO do we already have the peer but without the address?
                // TODO Update if already known peer
                Contact contact = newContact(peer.getSender());
                contact.setLastWifiContact(new Timestamp(peer.getLastSeen()));
                mContacts.add(contact);
                // TODO further information in this space?
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }
        for (RadarListener listener : mRadarListeners) {
            listener.onNewRadarContact(mContacts);
        }
    }

    @Override
    public void onInvitation(SyncComponent component) {
        PeerSTSet members = component.getMembers();
        ArrayList<Contact> contacts = new ArrayList<>();
        Enumeration<PeerSemanticTag> peerSemanticTagEnumeration = members.peerTags();
        while (peerSemanticTagEnumeration.hasMoreElements()) {
            PeerSemanticTag peerSemanticTag = peerSemanticTagEnumeration.nextElement();
            try {
                Contact byTag = getContactByTag(peerSemanticTag);
                contacts.add(byTag);
            } catch (SharkKBException e) {
                e.printStackTrace();
            }

        }

        L.d("onInvitation triggered!", this);
        try {
            ChatImpl chat = new ChatImpl(this, component, contacts, getMyProfile());
            mChatComponents.add(component);
//            for (EventListener listener : mEventListeners){
//                listener.onNewChat(chat);
//            }
        } catch (SharkKBException | JSONException e) {
            e.printStackTrace();
        }

    }

    public SemanticTag createUniqueChatId(List<Contact> recipients) throws SharkKBException {
        String name = "";
        for (Contact contact : recipients) {
            name += contact.getPST().getName();
        }
        name += System.currentTimeMillis();

        return InMemoSharkKB.createInMemoSemanticTag(name, name);
    }

    private List<Feed> getFeeds() throws SharkKBException {
        List<Feed> feeds = new ArrayList<>();
        Iterator<ASIPInformationSpace> iterator = mFeedKB.informationSpaces();
        while (iterator.hasNext()) {
            ASIPInformationSpace next = iterator.next();
            if (next == null) {
                continue;
            }
            // checks if the infoSpace has an type SemanticTag with the SI
            // from the mMessageType-Tag
            STSet types = next.getASIPSpace().getTypes();
            if (types.getSemanticTag(mFeedType.getSI()) != null) {
                Feed feed = new FeedImpl(this, mFeedKB, next);
                feeds.add(feed);
            }
        }
        return feeds;
    }

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

    // Misc
    //
    //


    // Facade Getter

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
        return new InMemoSharkKB(InMemoSharkKB.createInMemoSemanticNet(), InMemoSharkKB.createInMemoSemanticNet(), sharkKB.getPeersAsTaxonomy(), InMemoSharkKB.createInMemoSpatialSTSet(), InMemoSharkKB.createInMemoTimeSTSet());
    }

    public interface EventListener {
        void onNewChat(Chat chat);
    }
}
