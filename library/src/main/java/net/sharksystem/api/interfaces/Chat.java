package net.sharksystem.api.interfaces;

import net.sharkfw.knowledgeBase.SharkKBException;

import org.json.JSONException;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface for the Chat Implementation - A Chat represents a Conversation with one or more contacts
 */
public interface Chat extends Timeable {

	/**
	 * Add a Message to the Chat and sends it
	 * @param content
     */
	void sendMessage(Content content) throws SharkKBException;

	void sendMessage(InputStream inputStream, String message, String mimetype) throws JSONException, SharkKBException;

	void sendMessage(InputStream inputStream, String message, String mimetype, Contact sender) throws JSONException, SharkKBException;


	/**
	 * deletes the Chat from the KnowledgeBase
	 */
	void delete();

	/**
	 * returns a List of all Messages within the Chat
	 * @return
     */
    List<Message> getMessages(boolean ascending) throws SharkKBException;
	List<Message> getMessages(int startIndex, int stopIndex, boolean ascending) throws SharkKBException;
	List<Message> getMessages(Timestamp start, Timestamp stop, boolean ascending) throws SharkKBException;
	List<Message> getMessages(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean ascending) throws SharkKBException;
	List<Message> getMessages(String search, int startIndex, int stopIndex, boolean ascending) throws SharkKBException;


	/**
	 * Updates the chat in the Database
	 */
	void update();

	/**
	 * Returns a List of the Contact which are included in the Chat
	 * If there is just one Recipient the Method returns a List of the Size one
	 * @return
     */
	List<Contact> getContacts() throws SharkKBException;

	/**
	 * Set the Picture of the Chat
	 */
	void setPicture(InputStream picture, String mimeType) throws SharkKBException;

	/**
	 * returns he chat picture
	 * @return
     */
	Content getPicture();

	/**
	 * Set Title of he Chat. By Default its a String of all Nicknames of the Contact in the Chat
	 */
	void setTitle(String title) throws SharkKBException;

	/**
	 * Returns Title of the Chat
	 * @return
     */
	String getTitle() throws SharkKBException;

	/**
	 * Returns the ID of the Chat
	 */
	int getID();

	/**
	 * Returns the Profile of the owner of the Chat
	 */
	Contact getOwner() throws SharkKBException;

	/**
	 * Returns the Timestamp of the most recent Message
	 */
	Timestamp getTimestamp() throws SharkKBException;


	/**
	 * Adds Contact to the Chat
	 * @param contact
	 */
	void addContact(Contact contact) throws SharkKBException, JSONException;

	/**
	 * Removes the Contact from the chat
	 * @param contact
     */
	void removeContact(Contact contact) throws SharkKBException, JSONException;

	/**
	 * sets the Admin of the Chat
	 * @param admin
     */
    void setAdmin(Contact admin) throws SharkKBException, JSONException;

	/**
	 * returns the Admin of the Chat
	 * @return
     */
	Contact getAdmin() throws SharkKBException;



}
