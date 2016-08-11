package net.sharksystem.sharknet_api_android.interfaces;

import net.sharkfw.knowledgeBase.SharkKBException;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 * Interface for the Messages used in Chats
 */

public interface Message extends Timeable, ContainsContent {
	/**
	 * Returns the Date, Time when a message was created
	 * @return
     */
    Timestamp getTimestamp() throws SharkKBException;

	/**
	 * returns the Author of a message
	 * @return
     */
    Contact getSender() throws SharkKBException;
	/**
	 * returns the recipient of a message
	 */
    List<Contact> getRecipients();

	/**
	 * returns the content of a Message
	 * @return
     */
    Content getContent();


	/**
	 * valuates if a Message was signed
	 * @return
     */
    boolean isSigned();

	/**
	 * valuates if a message was encrypted
	 * @return
     */
    boolean isEncrypted();

	/**
	 * deletes the message from a chat
	 */
	void deleteMessage() throws SharkKBException;

	/**
	 * marks the Message as is disliked. Shark will collect dislikes and after an special amount it will inform the author
	 * @param isDisliked
	 */
	void setDisliked(boolean isDisliked);

	/**
	 * Returns if the Comment is isDisliked
	 */
	boolean isDisliked();

	/**
	 * returns if the Message is sent by the user
	 * @return
     */
	boolean isMine();

	/**
	 * Returns if the Message is verified (which means the signature is valid)
	 */
	boolean isVerified();

	/**
	 * Sets if Message is verified
	 * @param verified
     */
	void setVerified(boolean verified);

	/**
	 * Returns the Chat of the Message or constructs a new
	 * @return
     */
	Chat getChat() throws SharkKBException;

	/**
	 * Returns if the Message is already read
	 * @return
     */
	boolean isRead();

	/**
	 * Sets boolean if message is read
	 * @param read
     */
	void setRead(boolean read);


	/**
	 * Set the Status of the Message as Signed
	 * @param signed
     */
	void setSigned(boolean signed);

	/**
	 * Set the Status of the  Message as Encrypted
	 * @param encrypted
     */
	void setEncrypted(boolean encrypted);

	/**
	 * Returns if the Message was received through direct contact with sender
	 * @return
     */
	boolean isDirectReceived();

	/**
	 * Sets if Message was received through direct contact with sender
	 * @param directReceived
     */
	void setDirectReceived(boolean directReceived);

	void setContent(InputStream inputStream, String messageString, String mimeType) throws SharkKBException;
}
