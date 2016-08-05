package net.sharksystem.sharknet_api_android.interfaces;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.PeerSemanticTag;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents a Contact (a Person) in SharkNet
 */
public interface Contact {

    public PeerSemanticTag getPST() throws SharkKBException;

	/**
	 * returns the Nickname of the contact
	 * @return
     */
    public String getNickname() throws SharkKBException;
	public void setNickname(String nickname) throws SharkKBException;


	/**
	 * returns the UID of the contact
	 * @return
     */
    public String getUID() throws SharkKBException;
	public void setUID(String uid) throws SharkKBException;

	/**
	 * Returns the profilepicture of a contact
	 * @return
     */

    public Content getPicture() throws SharkKBException;
	public void setPicture(Content pic) throws IOException, SharkKBException;

	/**
	 * returns the PublicKey of the contact
	 * @return
     */
    public String getPublicKey() throws SharkKBException;
	public void setPublicKey(String publicKey) throws SharkKBException;
	public Timestamp getPublicKeyExpiration();
	public String getPublicKeyFingerprint();

	public void deleteKey();


	/**
	 * Deletes the Contact from the Database
	 */
	public void delete();

	/**
	 * updates a Contact in the Database
	 */
	public void update();


	/**
	 * returns a List of all Interests the profile is interested in
	 * @return
	 */
	public Interest getInterests();

	/**
	 * Method to evaluate is a Contact is equal to another
	 * @return
     */
	public boolean isEqual(Contact c);

	/**
	 * Method returns the Owner of the Contact
	 */
	public Profile getOwner();

	/**
	 * add a real name to the contact
	 * @param name
     */
	public void  addName(String name) throws SharkKBException;

	/**
	 * Returns the real name of the contact
	 * @return
     */
	public String getName() throws SharkKBException;

	/**
	 * spelling suggestion- addTelephoneNumber()
	 * add a Telephonnumber to the Contact. The Contact has a List which can include more phone numbers (no validation included)
	 * @param telephonnumber
     */
	public void addTelephonnumber(String telephonnumber) throws SharkKBException;

	/**
	 * Returns the List of Phone Numbers
	 * @return
     */
	public List<String> getTelephonnumber() throws SharkKBException;

	/**
	 * Adds/Or overwrites the Note to a contact
	 * @param note
     */
	public void addNote(String note) throws SharkKBException;

	/**
	 * Returns Notes to a contact
	 * @return
     */
	public String getNote() throws SharkKBException;

	/**
	 * sets the e-mail of a contact (no validation included)
	 * @param email
     */
	public void setEmail(String email) throws SharkKBException;

	/**
	 * returns e-mail of the contact
	 * @return
     */
	public String getEmail() throws SharkKBException;

	/**
	 * Returns the timestamp of the last direct wifi connection with the contact
	 * @return
     */
	public Timestamp getLastWifiContact();

	/**
	 * Sets the timestamp of the last direct wifi connection with the contact
	 * @param lastWifiContact
     */
	public void setLastWifiContact(Timestamp lastWifiContact);


	}
