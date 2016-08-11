package net.sharksystem.sharknet_api_android.interfaces;

import net.sharkfw.knowledgeBase.SharkKBException;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents the Main-Functionality of SharkNet
 */
public interface SharkNet {

	/**
	 * Returns a List of all personal Profiles
	 * @return
     */
    List<Profile> getProfiles() throws SharkKBException;

	/**
	 * Returns a list of all Feeds which should be displayed in the Timeline
	 * @return
     */
	List<Feed> getFeeds(boolean descending);
    List<Feed> getFeeds(int start_index, int stop_index, boolean descending);
	List<Feed> getFeeds(Interest i, int start_index, int stop_index, boolean descending);
	List<Feed> getFeeds(String search, int start_index, int stop_index, boolean descending);
	List<Feed> getFeeds(Timestamp start, Timestamp end, int start_index, int stop_index, boolean descending);

	/**
	 * returns a list of all safed contacts
	 * @return
     */
    List<Contact> getContacts();

	/**
	 * returns a list of all chats
	 * @return
     */
    List<Chat> getChats() throws SharkKBException;

	/**
	 * Initializes a new Feed an Safes it in the KnowledgeBase
	 */
	Feed newFeed(Content content, Interest interest, Contact sender);


	/**
	 * Initializes a Profile an Safes it in the KnowledgeBase
	 * @return
     */
	Profile newProfile(String nickname, String deviceID);

	/**
	 * Initializes a Chat and safes it in the KnowledgeBase
	 * @param recipients
	 */
	Chat newChat(List<Contact> recipients) throws SharkKBException;

	/**
	 * Adds a Contact to the KnowledgeBase
	 * @param nickname
	 * @param uid
	 */
	Contact newContact(String nickname, String uid, String publicKey);


	/**
	 * Set the Profile the User want to use during the session
	 * @param myProfile
	 * @param password
	 * @return true if the authentification was sucessfull, false if not
     */

	boolean setProfile(Profile myProfile, String password);

	/**
	 * Returns the Profile which is active at the moment
	 * @return
     */
	Profile getMyProfile();

	/**
	 * Exchange Contact via NFC (just Method, not implemented)
     */
	void exchangeContactNFC();

	/**
	 * Adds Listener to the List of Listener
 	 * @param p
	 * @param listener
     */
	void addListener(Profile p, GetEvents listener);

	/**
	 * Removes a Listener
	 * @param p
	 * @param listener
     */
	void removeListener(Profile p, GetEvents listener);

	/**
	 * Methods calls all Methods of registered listeners when the profile matches a contact in the recipientslist of the message
	 * @param m
     */
	void informMessage(Message m) throws SharkKBException;

	/**
	 * Methods calls all Methods of registered listeners and informs about feed
	 * @param f
     */
	void informFeed(Feed f) throws SharkKBException;

	/**
	 * Methods calls all Methods of registered listeners and informs about comment
	 * @param c
     */
	void informComment(Comment c) throws SharkKBException;

	/**
	 * Methods calls all Methods of registered listeners and informs about contact (for example when exchanged via NFC)
	 * @param c
	 */
	void informContact(Contact c);


}
