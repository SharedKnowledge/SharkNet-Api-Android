//package net.sharksystem.api.impl;
//
//import android.app.Activity;
//import android.content.Context;
//
//import net.sharkfw.asip.ASIPKnowledge;
//import net.sharkfw.asip.ASIPSpace;
//import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
//import net.sharkfw.knowledgeBase.PeerSTSet;
//import net.sharkfw.knowledgeBase.PeerSemanticTag;
//import net.sharkfw.knowledgeBase.SharkKB;
//import net.sharkfw.knowledgeBase.SharkKBException;
//import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
//import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
//import net.sharkfw.knowledgeBase.sync.manager.SyncManager.SyncInviteListener;
//import net.sharkfw.system.SharkNotSupportedException;
//import net.sharksystem.api.interfaces.Contact;
//import net.sharksystem.api.interfaces.RadarListener;
//import net.sharksystem.api.shark.peer.AndroidSharkEngine;
//import net.sharksystem.api.shark.peer.NearbyPeer;
//import net.sharksystem.api.shark.peer.NearbyPeerManager;
//import net.sharksystem.api.shark.ports.NfcPkiPort;
//import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;
//import net.sharksystem.api.shark.ports.NfcPkiPortListener;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Enumeration;
//
///**
// * Created by j4rvis on 01.08.16.
// */
//public class SharkNetEngine implements NearbyPeerManager.NearbyPeerListener, SyncInviteListener {
//
//    private static SharkNetEngine sInstance = null;
//    private AndroidSharkEngine mSharkEngine;
//    private ArrayList<RadarListener> mRadarListeners = new ArrayList<>();
//    // Shark
//    private SharkKB mRootKB = null;
//    private Context mContext;
//
//    private SharkNetEngine() {
//        mRootKB = new InMemoSharkKB();
//    }
//
//    public static SharkNetEngine getSharkNet() {
//        if (SharkNetEngine.sInstance == null) {
//            sInstance = new SharkNetEngine();
//        }
//        return sInstance;
//    }
//
//    public void setContext(Context context) {
//        mContext = context;
//        mSharkEngine = new AndroidSharkEngine(mContext, mRootKB);
//    }
//
//    public void startShark() throws SharkKBException, SharkProtocolNotSupportedException, IOException {
//
////        Profile profile = getMyProfile();
////        mSharkEngine.setEngineOwnerPeer(profile.getPST());
//
////        Interest interests = profile.getInterests();
//        ASIPSpace asipSpace = null;
////        if (interests != null) {
////            asipSpace = interests.asASIPSpace();
////        }
////        mSharkEngine.setSpace(asipSpace);
//
//        // Sync
//        mSharkEngine.getSyncManager().allowInvitation(true);
//        mSharkEngine.getSyncManager().addInviteListener(this);
//
//        // Discovery
//        mSharkEngine.addNearbyPeerListener(this);
////        mSharkEngine.startBluetooth();
////        mSharkEngine.startDiscovery();
//
//    }
//
//
//    public void addRadarListener(RadarListener listener) {
//        if (!mRadarListeners.contains(listener)) {
//            mRadarListeners.add(listener);
//        }
//    }
//
//
//    public void removeRadarListener(RadarListener listener) {
//        if (!mRadarListeners.contains(listener)) {
//            mRadarListeners.remove(listener);
//        }
//    }
//
//
//    /**
//     * Creates the NfcPkiPort and sets the {{@link NfcPkiPortListener}} so
//     * that the UI gets the
//     * information about received messages etc. Also returns the
//     * {{@link NfcPkiPortEventListener}}
//     * so that the port can react on the decisions.
//     *
//     * @param listener
//     * @return {{@link NfcPkiPortEventListener}} to set as listener
//     */
//
//    public NfcPkiPortEventListener setupNfc(Activity activity, NfcPkiPortListener listener, ASIPKnowledge knowledge) throws SharkProtocolNotSupportedException, SharkNotSupportedException {
//        // Create the nfcPort w/ the listener set
//        NfcPkiPort nfcPkiPort = new NfcPkiPort(mSharkEngine, listener);
//        mSharkEngine.setupNfc(activity, nfcPkiPort);
//        mSharkEngine.stopNfc();
//        mSharkEngine.offer(knowledge);
//        return nfcPkiPort;
//    }
//
//
//    public void startSendingViaNfc() throws SharkProtocolNotSupportedException, IOException {
//        mSharkEngine.startNfc();
//    }
//
//    @Override
//    public void onNearbyPeerFound(ArrayList<NearbyPeer> peers) {
////        mContacts = new ArrayList<>();
////
////        for (NearbyPeer peer : peers) {
////            try {
////                // TODO do we already have the peer but without the address?
////                // TODO Update if already known peer
////                Contact contact = newContact(peer.getSender());
////                contact.setLastWifiContact(new Timestamp(peer.getLastSeen()));
////                mContacts.add(contact);
////                // TODO further information in this space?
////            } catch (SharkKBException e) {
////                e.printStackTrace();
////            }
////        }
////        for (RadarListener listener : mRadarListeners) {
//////            listener.onNewRadarContact(mContacts);
////        }
//    }
//
//    @Override
//    public void onInvitation(SyncComponent component) {
////        PeerSTSet members = component.getMembers();
////        ArrayList<Contact> contacts = new ArrayList<>();
////        Enumeration<PeerSemanticTag> peerSemanticTagEnumeration = members.peerTags();
////        while (peerSemanticTagEnumeration.hasMoreElements()) {
////            PeerSemanticTag peerSemanticTag = peerSemanticTagEnumeration.nextElement();
////            try {
////                Contact byTag = getContactByTag(peerSemanticTag);
////                contacts.add(byTag);
////            } catch (SharkKBException e) {
////                e.printStackTrace();
////            }
//
//        }
//
////        L.d("onInvitation triggered!", this);
////        try {
////            ChatImpl chat = new ChatImpl(this, component, contacts, getMyProfile());
////            mChatComponents.add(component);
//////            for (EventListener listener : mEventListeners){
//////                listener.onNewChat(chat);
//////            }
////        } catch (SharkKBException | JSONException e) {
////            e.printStackTrace();
////        }
//
//    }
//}
