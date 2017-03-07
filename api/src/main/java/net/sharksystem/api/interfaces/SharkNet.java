package net.sharksystem.api.interfaces;

import android.app.Activity;

import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;
import net.sharksystem.api.shark.ports.NfcPkiPortListener;
import net.sharksystem.api.shark.protocols.nfc.NfcMessageStub;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 * <p>
 * Interface represents the Main-Functionality of SharkNet
 */
public interface SharkNet {

    List<Contact> getRadarContacts() throws SharkKBException;

    void addRadarListener(RadarListener listener);

    void removeRadarListener(RadarListener listener);

    /**
     * Returns a List of all personal Profiles
     *
     * @return
     */
    List<Profile> getProfiles() throws SharkKBException;

    /**
     * Returns a list of all Feeds which should be displayed in the Timeline
     *
     * @return
     */
    List<Feed> getFeeds(boolean ascending) throws SharkKBException;

    List<Feed> getFeeds(int start_index, int stop_index, boolean ascending) throws SharkKBException;

    List<Feed> getFeeds(Interest i, int start_index, int stop_index, boolean ascending);

    List<Feed> getFeeds(String search, int start_index, int stop_index, boolean ascending) throws SharkKBException;

    List<Feed> getFeeds(Timestamp start, Timestamp end, int start_index, int stop_index, boolean ascending) throws SharkKBException;

    /**
     * returns a list of all safed contacts
     *
     * @return
     */
    List<Contact> getContacts() throws SharkKBException;

    /**
     * returns a list of all chats
     *
     * @return
     */
    List<Chat> getChats() throws SharkKBException;

    Chat getChatById(String id) throws SharkKBException;

    /**
     * Initializes a new Feed an Safes it in the KnowledgeBase
     */
    Feed newFeed(Content content, Interest interest, Contact sender) throws SharkKBException, JSONException;

    Feed newFeed(InputStream stream, String mimeType, String message, Interest interest, Contact sender) throws SharkKBException, JSONException;

    /**
     * Initializes a Profile an Safes it in the KnowledgeBase
     *
     * @return
     */
    Profile newProfile(String nickname, String deviceID) throws SharkKBException;

    Profile newProfile(PeerSemanticTag tag) throws SharkKBException;

    /**
     * Initializes a Chat and safes it in the KnowledgeBase
     *
     * @param recipients
     */
    Chat newChat(List<Contact> recipients) throws SharkKBException, JSONException;

    /**
     * Adds a Contact to the KnowledgeBase
     *
     * @param nickname
     * @param uid
     */
    Contact newContact(String nickname, String uid, String publicKey) throws SharkKBException;

    /**
     * Adds a Contact to the KnowledgeBase
     *
     * @param nickName
     * @param uId
     */
    Contact newContact(String nickName, String uId) throws SharkKBException;

    Contact newContact(PeerSemanticTag tag) throws SharkKBException;

    /**
     * Set the Profile the User want to use during the session
     *
     * @param myProfile
     * @param password
     * @return true if the authentification was sucessfull, false if not
     */

    boolean setActiveProfile(Profile myProfile, String password) throws SharkKBException, JSONException;

    /**
     * Returns the Profile which is active at the moment
     *
     * @return
     */
    Profile getMyProfile() throws SharkKBException;

    /**
     * Exchange Contact via NFC
     */
    NfcPkiPortEventListener setupNfc(Activity activity, NfcPkiPortListener listener, ASIPKnowledge knowledge) throws SharkProtocolNotSupportedException, SharkNotSupportedException;

    /**
     * Activate Reader mode for the device
     */
    void startSendingViaNfc() throws SharkProtocolNotSupportedException, IOException;
}
