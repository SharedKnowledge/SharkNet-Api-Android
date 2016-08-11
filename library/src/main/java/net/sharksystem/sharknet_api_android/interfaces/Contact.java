package net.sharksystem.sharknet_api_android.interfaces;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.PeerSemanticTag;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents a Contact (a Person) in SharkNet
 */
public interface Contact {

    PeerSemanticTag getPST() throws SharkKBException;

	/**
	 * returns the Nickname of the contact
	 * @return
     */
    String getNickname() throws SharkKBException;
	void setNickname(String nickname) throws SharkKBException;


	/**
	 * returns the UID of the contact
	 * @return
     */
    String getUID() throws SharkKBException;
	void setUID(String uid) throws SharkKBException;

	/**
	 * Returns the profile picture of a contact
	 * @return
     */

    Content getPicture() throws SharkKBException;
	void setPicture(InputStream is, String title, String mimeType) throws IOException, SharkKBException;

	/**
	 * returns the PublicKey of the contact
	 * @return
     */
    String getPublicKey() throws SharkKBException;
	void setPublicKey(String publicKey) throws SharkKBException;
	Timestamp getPublicKeyExpiration();
	String getPublicKeyFingerprint();

	void deleteKey();

	/**
	 * Deletes the Contact from the Database
	 */
	void delete();

	/**
	 * updates a Contact in the Database
	 */
	void update();


	/**
	 * returns a List of all Interests the profile is interested in
	 * @return
	 */
	Interest getInterests();

	/**
	 * Method to evaluate is a Contact is equal to another
	 * @return
     */
	boolean isEqual(Contact c);

	/**
	 * Method returns the Owner of the Contact
	 */
	Profile getOwner();

	/**
	 * add a real name to the contact
	 * @param name
     */
	void addName(String name) throws SharkKBException;

	/**
	 * Returns the real name of the contact
	 * @return
     */
	String getName() throws SharkKBException;

	/**
	 * spelling suggestion- addTelephoneNumber()
	 * add a Telephonnumber to the Contact. The Contact has a List which can include more phone numbers (no validation included)
	 * @param telephonnumber
     */
	void addTelephonnumber(String telephonnumber) throws SharkKBException;

	/**
	 * Returns the List of Phone Numbers
	 * @return
     */
	List<String> getTelephonnumber() throws SharkKBException;

	/**
	 * Adds/Or overwrites the Note to a contact
	 * @param note
     */
	void addNote(String note) throws SharkKBException;

	/**
	 * Returns Notes to a contact
	 * @return
     */
	String getNote() throws SharkKBException;

	/**
	 * sets the e-mail of a contact (no validation included)
	 * @param email
     */
	void setEmail(String email) throws SharkKBException;

	/**
	 * returns e-mail of the contact
	 * @return
     */
	String getEmail() throws SharkKBException;

	/**
	 * Returns the timestamp of the last direct wifi connection with the contact
	 * @return
     */
	Timestamp getLastWifiContact();

	/**
	 * Sets the timestamp of the last direct wifi connection with the contact
	 * @param lastWifiContact
     */
	void setLastWifiContact(Timestamp lastWifiContact);

}
