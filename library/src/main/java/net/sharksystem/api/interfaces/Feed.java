package net.sharksystem.api.interfaces;

import net.sharkfw.knowledgeBase.Interest;
import net.sharkfw.knowledgeBase.SharkKBException;

import org.json.JSONException;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents the Feed-Functionality (Timeline)
 */
public interface Feed extends Timeable, ContainsContent{

	void setInterest(Interest interest) throws SharkKBException;

	/**
	 * Returns the name of the interest the feed references to
	 * @return
     */
    Interest getInterest() throws SharkKBException;

	/**
	 * Returns Date and Time when the feed was created
	 * @return
     */
    Timestamp getTimestamp() throws SharkKBException;

	void setContent(InputStream inputStream, String messageString, String mimeType) throws SharkKBException;

	/**
	 * returns the content of a Message
	 * @return
	 */
    Content getContent() throws SharkKBException;

	void setSender(Contact sender) throws SharkKBException, JSONException;

	/**
	 * returns the Author of a Feed
	 * @return
	 */
    Contact getSender() throws SharkKBException;

	/**
	 * Deletes the Feed from the Database
	 */
	void delete() throws SharkKBException;

	/**
	 * adds and safes a comment to a feed
	 * @param comment
	 */
	void newComment(Content comment, Contact author);
	/**
	 * Returns a List of comments referencing the feed
	 * @return
     */
    List<Comment> getComments(boolean ascending) throws SharkKBException;
	List<Comment> getComments(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean ascending) throws SharkKBException;
	List<Comment> getComments(int startIndex, int stopIndex, boolean ascending) throws SharkKBException;
	List<Comment> getComments(Timestamp start, Timestamp stop, boolean ascending) throws SharkKBException;

	List<Comment> getComments(String search, int startIndex, int stopIndex, boolean ascending) throws SharkKBException;

	/**
	 * Marks this Feed as disliked, Shark will collect dislikes and after an special amount it will inform the author
	 *
	 * @param disliked if true the feed is marked as disliked or liked
	 */
	void setDisliked(boolean disliked) throws SharkKBException;


	/**
	 * Returns if the Comment is isDisliked
	 */
	boolean isDisliked() throws SharkKBException;
}
