package net.sharksystem.sharknet_api_android.dummy_impl;

import android.provider.MediaStore;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.sharknet_api_android.interfaces.Chat;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Message;
import net.sharksystem.sharknet_api_android.interfaces.Profile;
import net.sharksystem.sharknet_api_android.dummy_impl.utils.Resources;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by timol on 16.05.2016.
 */

public class ImplChat implements Chat {

	String title;
	Content picture;
	int id;
	Profile owner;
	Contact admin;
	Timestamp lastmessage;

	/**
	 * Constructor for new Chat which is going to be saved in the DB
	 * @param contact_List
	 * @param owner
     */

	public ImplChat(List <Contact> contact_List, Profile owner){
		this.owner = owner;
		this.admin = owner;
		safeContactList(contact_List);
		setDefaultTitle();
		setID();
		setDefaultLastMessage();
		save();
	}

	/**
	 * Generates a Chat from a new Message
	 * @param m
	 * @param owner
     */

	public ImplChat(Message m, Profile owner) throws SharkKBException {
		this.owner  = owner;
		this.admin = m.getSender();
		List<Contact> contactList = m.getRecipients();
		contactList.remove(owner);
		contactList.add(m.getSender());
		safeContactList(contactList);
		setID();
		try {
			lastmessage = m.getTimestamp();
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		setDefaultTitle();
	}

	/**
	 * Constructor for Chats from the Database which are not going to be saved
	 * @param contact_List
	 * @param title
	 * @param picture
	 * @param id
	 * @param owner
	 * @param admin
     * @param lastmessage
     */
	public ImplChat(List<Contact> contact_List, String title, Content picture, int id, Profile owner, Contact admin, Timestamp lastmessage) {
		this.title = title;
		this.picture = picture;
		this.id = id;
		this.owner = owner;
		this.admin = admin;
		this.lastmessage = lastmessage;
		safeContactList(contact_List);
	}



	@Override
	public void sendMessage(Content content) throws SharkKBException {
		Message m = new ImplMessage(content, getContacts(), owner, owner);
		m.setRead(true);
	}

	@Override
	public void sendMessage(InputStream inputStream, String message, String mimetype) throws JSONException, SharkKBException {

	}

	@Override
	public void delete() {
		//ToDo: Shark - delete Chat from Database
		//DummyDB implementation
		DummyDB.getInstance().removeChat(this);
	}

	@Override
	public List<Message> getMessages(boolean descending) {
		//ToDo: Shark - find Messages blonging to the chat AND ALSO THE OWNER OF THE CHAT (which is the currently aktive profile and fill List of Messages
		//DummyDB Implememntation
		List <Message> message_list = null;
		try {
			message_list = DummyDB.getInstance().getMessageList(this, descending);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return message_list;
	}

	@Override
	public List<Message> getMessages(int startIndex, int stopIndex, boolean descending) {
		//ToDo: Shark - fill List with Messages from the chat within the given intervall - sorted by time
		List <Message> message_list = null;
		try {
			message_list = DummyDB.getInstance().getMessageList(this, startIndex, stopIndex, descending);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return message_list;

	}

	@Override
	public List<Message> getMessages(Timestamp start, Timestamp stop, boolean descending) {
		//ToDo: Shark - fill List with Messages from the chat within the given timerange - sorted by time
		List <Message> message_list = null;
		try {
			message_list = DummyDB.getInstance().getMessageList(this, start, stop, descending);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return message_list;
	}

	@Override
	public List<Message> getMessages(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean descending) {
		//ToDo: Shark - fill List with Messages from the chat within the given intervall and timerange - sorted by time
		List <Message> message_list = null;
		try {
			message_list = DummyDB.getInstance().getMessageList(this, startIndex, stopIndex, start, stop, descending);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return message_list;

	}

	@Override
	public List<Message> getMessages(String search, int startIndex, int stopIndex, boolean descending) {
		//ToDo: Shark - fill List with Messages from the chat within the given intervall and containing search string - sorted by time
		List <Message> message_list = null;
		try {
			message_list = DummyDB.getInstance().getMessageList(this, search, startIndex, stopIndex, descending);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return message_list;
	}

	/**
	 * Save Chat to the DB
	 */
	private void save() {
		//ToDo: Shark - Safe Chat to the Database
		//DummmyDB implementaion
		DummyDB.getInstance().addChat(this);
	}

	@Override
	public void update() {
		//ToDo: Shark - Update Chat in KB
	}

	@Override
	public List<Contact> getContacts() {
		return DummyDB.getInstance().getChatContacts(this);
	}

	@Override
	public void setPicture(InputStream picture, String mimeType) throws SharkKBException {

	}

	public void setPicture(Content picture) {
		this.picture = picture;
	}

	@Override
	public Content getPicture() {
		return picture;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public Profile getOwner() {
		return owner;
	}

	@Override
	public Timestamp getTimestamp() {
		//ToDo: Shark - get Timestamp from the most recent Message
		try {
			if(!DummyDB.getInstance().getMessageList(this, true).isEmpty()){
                lastmessage = DummyDB.getInstance().getMessageList(this, true).get(0).getTimestamp();
            }
		} catch (SharkKBException e) {
			e.printStackTrace();
		}

		return lastmessage;
	}

	@Override
	public void addContact(Contact contact) throws SharkKBException, JSONException {

	}

	@Override
	public void removeContact(Contact contact) throws SharkKBException, JSONException {

	}

	public void addContact(List<Contact> cList) {
		//ToDo: Shark - Implement that Contacts will added to the Chat in the Shark DB
		List<Contact> contactList = DummyDB.getInstance().getChatContacts(this);
		for(Contact newc : cList){
			if(!contactList.contains(newc)){
				contactList.add(newc);
			}
		}
	}

	public void removeContact(List<Contact> cList){

		//ToDo: Shark - Implement that Contacts will be removed from the Chat in the Shark DB
		List<Contact> contactList = DummyDB.getInstance().getChatContacts(this);
		for(Contact remc : cList){
			if(contactList.contains(remc)){
				contactList.remove(remc);
			}
		}
	}

	@Override
	public void setAdmin(Contact admin) {
		this.admin = admin;
	}

	@Override
	public Contact getAdmin() {
		return admin;
	}

	/**
	 * This Method is used to give the Chat a random ID including to check within the Database that the id is unique
	 */
	private void setID(){

		Random rand = new Random();
		int randomNum = rand.nextInt();

		//ToDo: Shark - Validate that Id ist unique (just in the Local KB not in the hole Sharknet-Network)
		//TODo: Shark - Check if the Chat already have a ID in the KB and use it, the chat can be find with the contact_list
		while(!DummyDB.getInstance().validChatID(randomNum)){
			randomNum = rand.nextInt(Integer.SIZE - 1);
		}
		id = randomNum;
	}

	/**
	 * Sets the default Title which is the Nickname of all Contacts in the List
	 */
	private void setDefaultTitle(){
		String[] title_array = new String[getContacts().size()];
		int i = 0;
		for(Contact c : getContacts()){
			try {
				title_array[i] = c.getNickname();
			} catch (SharkKBException e) {
				e.printStackTrace();
			}
			i++;
		}

		StringBuilder builder = new StringBuilder();
		for(String s : title_array) {
			builder.append(s);
			builder.append(" ");
		}
		title = builder.toString();
	}
//
//	/**
//	 * Sets a default picture. The Picture of the first Contact in the List
//	 */
//
//	private void setDefaultPic() {
//		ImplContent piccon;
//		File pic = null;
//		if (getContacts().size() > 1) {
//			pic = Resources.get("group.png");
//			piccon = new ImplContent(owner);
//			piccon.setFile(pic);
//		} else try {
//			if (getContacts().get(0).getPicture() == null) {
//                pic = Resources.get("person.png");
//                piccon = new ImplContent(owner);
//                piccon.setFile(pic);
//            } else {
//                piccon = (ImplContent) getContacts().get(0).getPicture();
//            }
//		} catch (SharkKBException e) {
//			e.printStackTrace();
//		}
//		if(pic != null){
//			try {
//				String mimeType = MediaStore.Files.probeContentType(pic.toPath());
//				piccon.setMimeType(mimeType);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		setPicture(piccon);
//	}



	/**
	 * Sets the default Value for Timestamp lastmessage which is the creation date
	 */
	private void setDefaultLastMessage(){
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		lastmessage = new Timestamp(now.getTime());
	}

	/**
	 * Private Method to SetContactList in Database
	 */
	private void safeContactList(List<Contact> contact_list) {
		//ToDo: Shark - Safe ContactList int the Database
		//DummmyDB implementaion
		DummyDB.getInstance().setChatContacts(this, contact_list);
	}


}

