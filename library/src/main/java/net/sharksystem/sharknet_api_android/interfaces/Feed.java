package net.sharksystem.sharknet_api_android.interfaces;

import net.sharkfw.knowledgeBase.SharkKBException;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents the Feed-Functionality (Timeline)
 */
public interface Feed extends Timeable, ContainsContent{
	/**
	 * Returns the name of the interest the feed references to
	 * @return
     */
    Interest getInterest();

	/**
	 * Returns Date and Time when the feed was created
	 * @return
     */
    Timestamp getTimestamp();
	/**
	 * returns the content of a Message
	 * @return
	 */

    Content getContent();
	/**
	 * returns the Author of a Feed
	 * @return
	 */
    Contact getSender();

	/**
	 * Returns a List of comments referencing the feed
	 * @return
     */
    List<Comment> getComments(boolean descending) throws SharkKBException;
	List<Comment> getComments(int startIndex, int stopIndex, boolean descending) throws SharkKBException;
	List<Comment> getComments(Timestamp start, Timestamp stop, boolean descending) throws SharkKBException;
	List<Comment> getComments(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean descending) throws SharkKBException;
	List<Comment> getComments(String search, int startIndex, int stopIndex, boolean descending) throws SharkKBException;


	/**
	 * adds and safes a comment to a feed
	 * @param comment
	 */
	void newComment(Content comment, Contact author);

	/**
	 * Deletes the Feed from the Database
	 */
	void delete();
	/**
	 * Marks this Feed as disliked, Shark will collect dislikes and after an special amount it will inform the author
	 *
	 * @param disliked if true the feed is marked as disliked or liked
	 */
	void setDisliked(boolean disliked);

	/**
	 * Returns if the Comment is isDisliked
	 */
	boolean isDisliked();

	/**
	 * Returns the Owner of a Feed
	 * @return
     */
	Profile getOwner();


}
