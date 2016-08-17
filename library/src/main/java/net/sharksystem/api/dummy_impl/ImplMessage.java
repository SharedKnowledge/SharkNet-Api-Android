package net.sharksystem.api.dummy_impl;


import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.api.interfaces.Profile;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplMessage implements Message {

	Profile owner;
	Contact sender;
	List<Contact> recipient_list;
	Timestamp time;
	boolean isSigned;
	boolean isEncrypted;
	boolean isVerified;
	boolean dierectRecived;
	Content content;
	Boolean disliked = false;
	Boolean read = false;

	/**
	 * Constructor for Messages which are from the Datebase and are not going to be sended, just used by the API to fill List of Messages
	 * @param content
	 * @param time
	 * @param sender
	 * @param recipient_list
	 * @param isSigned
     * @param isEncrypted
     */
	public ImplMessage(Content content, Timestamp time, Contact sender, Profile owner, List<Contact> recipient_list, boolean isSigned, boolean isEncrypted){
		this.content = content;
		this.time = time;
		this.sender = sender;
		this.recipient_list= recipient_list;
		this.isSigned = isSigned;
		this.isEncrypted = isEncrypted;
		this.owner = owner;

	}

	/**
	 * Constuctor for New Messages that are going to be sended
	 * @param content
	 * @param recipient_list
     */

	public ImplMessage(Content content, List<Contact> recipient_list, Contact sender, Profile owner) throws SharkKBException {
		this.content = content;
		this.owner = owner;
		this.recipient_list = recipient_list;
		this.sender = sender;
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		time = new Timestamp(now.getTime());
		sendMessage();
	}

	/**
	 * writes the Message in the Database and sends it, is only called by the constructor for new Messages
	 */
	private void sendMessage() throws SharkKBException {
		//ToDo: Shark - send the message, saveing is in the Method Save
		//DummyDB Implementation
		save(getChat());
	}

	/**
	 * Saves Message to the Database, this is only used for incoming messages (no sending)
	 */
	private void save(Chat c) throws SharkKBException {
		//ToDo: Shark - Save the Message to the Database
		if(!c.getMessages(true).contains(this)){
			DummyDB.getInstance().addMessage(this, c);
		}
	}


	@Override
	public void setSigned(boolean signed) {
		isSigned = signed;
	}

	@Override
	public void setEncrypted(boolean encrypted) {
		isEncrypted = encrypted;
	}

	@Override
	public Timestamp getTimestamp() {
		return time;
	}
	@Override
	public Contact getSender() {
		return sender;
	}

	@Override
	public List<Contact> getRecipients() {
		return recipient_list;
	}

	@Override
	public Content getContent() {
		return content;
	}

	@Override
	public boolean isSigned() {
		return isSigned;
	}

	@Override
	public boolean isEncrypted() {
		return isEncrypted;
	}

	@Override
	public void deleteMessage() throws SharkKBException {
		//ToDo: Shark - delete the message from the Database

		Chat chat = getChat();
		DummyDB.getInstance().removeMessage(this, chat);

	}

	@Override
	public void setDisliked(boolean isDisliked) {
		disliked = isDisliked;
		//ToDo: Shark - safe that the message was isDisliked
	}

	/**
	 * Method is called to find the Chat the Message belongs to in the DummyDB
	 * if there is no existing Chat the Mehtod returns a new one
	 * @return
     */

	@Override
	public Chat getChat() throws SharkKBException {
		//Implementation of DummyDB
		//ToDo: Shark - lookup for the Chat
		DummyDB db = DummyDB.getInstance();
		List<Chat> chats = db.getChat_list(owner);
		for(Chat c : chats){
			List<Contact> cs = c.getContacts();
			if(cs.equals(recipient_list)){
				save(c);
				return c;
			}
		}
		Chat newChat = new ImplChat(this, owner);
		save(newChat);
		return newChat;
	}

	@Override
	public boolean isRead() {
		return read;
	}

	@Override
	public void setRead(boolean read) {
		this.read = read;
	}


	@Override
	public boolean isMine(){
		if(sender.isEqual(owner)){
			return true;
		}
		else return false;
	}

	@Override
	public boolean isVerified() {
		return isVerified;
	}

	@Override
	public void setVerified(boolean verified){
		this.isVerified = verified;
	}

	@Override
	public boolean isDisliked() {
		return disliked;
	}


	@Override
	public boolean isDirectReceived() {
		return dierectRecived;
	}

	@Override
	public void setDirectReceived(boolean dierectRecived) {
		this.dierectRecived = dierectRecived;
	}

	@Override
	public void setContent(InputStream inputStream, String messageString, String mimeType) throws SharkKBException {

	}

}
