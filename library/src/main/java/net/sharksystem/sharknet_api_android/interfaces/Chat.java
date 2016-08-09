package net.sharksystem.sharknet_api_android.interfaces;

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
	/**
	 * deletes the Chat from the KnowledgeBase
	 */
	void delete();

	/**
	 * returns a List of all Messages within the Chat
	 * @return
     */
    List<Message> getMessages(boolean descending);
	List<Message> getMessages(int startIndex, int stopIndex, boolean descending);
	List<Message> getMessages(Timestamp start, Timestamp stop, boolean descending);
	List<Message> getMessages(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean descending);
	List<Message> getMessages(String search, int startIndex, int stopIndex, boolean descending);


	/**
	 * Updates the chat in the Database
	 */
	void update();

	/**
	 * Returns a List of the Contact wich are included in the Chat
	 * If there is just one Recipient the Method returns a List of the Size one
	 * @return
     */
	List<Contact> getContacts();

	/**
	 * Set the Picture of the Chat
	 */
	void setPicture(Content picture);

	/**
	 * returns he chatpicture
	 * @return
     */
	Content getPicture();

	/**
	 * Set Title of he Chat. By Default its a String of all Nicknames of the Contact in the Chat
	 */
	void setTitle(String title);

	/**
	 * Returns Title of the Chat
	 * @return
     */
	String getTitle();

	/**
	 * Returns the ID of the Chat
	 */
	int getID();

	/**
	 * Returns the Profile of the owner of the Chat
	 */
	Contact getOwner();

	/**
	 * Returns the Timestamp of the most recent Message
	 */
	Timestamp getTimestamp();


	/**
	 * Adds Contacts to the Chat
	 * @param cList
	 */
	void addContact(List<Contact> cList);

	/**
	 * Removes the Contact from the chat
	 * @param cList
     */
	void removeContact(List<Contact> cList);

	/**
	 * sets the Admin of the Chat
	 * @param admin
     */
    void setAdmin(Contact admin);

	/**
	 * returns the Admin of the Chat
	 * @return
     */
	Contact getAdmin();



}
