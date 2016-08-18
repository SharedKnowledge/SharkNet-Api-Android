package net.sharksystem.api.dummy_impl;

import net.sharkfw.knowledgeBase.Interest;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Comment;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.api.interfaces.Profile;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplFeed implements Feed {

	Content content;
	Profile owner;
	Timestamp datetime;
	Interest interest;
	Contact sender;
	Boolean isDisliked = false;


	/**
	 * This constructor is used to construct new Feeds which are going to be saved in the Database and sended
	 * @param content
	 * @param interest
	 * @param sender
     */
	public ImplFeed(Content content, Interest interest, Contact sender, Profile owner){
		this.content = content;
		this.interest = interest;
		this.sender = sender;
		this. owner = owner;
		datetime = new Timestamp(new Date().getTime());
		save();
	}

	/**
	 * This Constructor is used to construct the Feeds already in the Database, it will not send or safe them
	 * @param content
	 * @param interest
	 * @param sender
     * @param datetime
     */
	public ImplFeed(Content content, Interest interest, Contact sender, Timestamp datetime, Profile owner){
		this.sender = sender;
		this.interest = interest;
		this.content = content;
		this.datetime = datetime;
		this.owner = owner;
	}

	@Override
	public String getId() throws SharkKBException {
		return null;
	}

	@Override
	public void setInterest(Interest interest) {

	}

	@Override
	public Interest getInterest() {
		return interest;
	}

	@Override
	public Timestamp getTimestamp() {
		return datetime;
	}

	@Override
	public void setContent(InputStream inputStream, String messageString, String mimeType) throws SharkKBException {

	}

	@Override
	public Content getContent() {
		return content;
	}

	@Override
	public void setSender(Contact sender) {

	}

	@Override
	public Contact getSender() {
		return sender;
	}

	@Override
	public List<Comment> getComments(boolean ascending) throws SharkKBException {
		//ToDo: Shark - search for comments construct the objects and fill the list - sorted by time
		List<Comment> commentlist = DummyDB.getInstance().getComments(this, ascending);
		return commentlist;
	}

	@Override
	public List<Comment> getComments(int startIndex, int stopIndex, boolean ascending) throws SharkKBException {
		//ToDo: Shark - search for comments within the intervall - sorted by time
		List<Comment> commentlist = DummyDB.getInstance().getComments(this, startIndex, stopIndex, ascending);
		return commentlist;
	}

	@Override
	public List<Comment> getComments(Timestamp start, Timestamp stop, boolean ascending) throws SharkKBException {
		//ToDo: Shark - search for comments within the timerange  - sorted by time
		List<Comment> commentlist = DummyDB.getInstance().getComments(this, start, stop, ascending);
		return commentlist;
	}

	@Override
	public List<Comment> getComments(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean ascending) throws SharkKBException {
		//ToDo: Shark - search for comments within the timerange and intervall - sorted by time
		List<Comment> commentlist = DummyDB.getInstance().getComments(this, startIndex, stopIndex, start, stop, ascending);
		return commentlist;
	}

	@Override
	public List<Comment> getComments(String search, int startIndex, int stopIndex, boolean ascending) throws SharkKBException {
		//ToDo: Shark - search for comments within the intervall containing the searchstring - sorted by time
		List<Comment> commentlist = DummyDB.getInstance().getComments(this, search,  startIndex, stopIndex, ascending);
		return commentlist;
	}


	@Override
	public Comment newComment(Content comment, Contact author) {
		return new ImplComment(comment, author, this, owner);
	}

	@Override
	public Comment newComment(InputStream stream, String message, String mimeType) {
		return null;
	}

	/**
	 * Saves the Feed in the DB
	 */

	private void save() {
		//ToDo: Shark - safe Feed in KB and sends it
		//Implementation of DummyDB
		DummyDB.getInstance().addfeed(this);
	}

	@Override
	public void delete() {
		//ToDo: Shark - delte Feed in KB
		//Implementation of DummyDB
		DummyDB.getInstance().removefeed(this);
	}

	@Override
	public void setDisliked(boolean disliked) {
		isDisliked = disliked;
		//ToDo: Shark - safe that the message was isDisliked
	}

	@Override
	public boolean isDisliked() {
		return isDisliked;
	}
}

