package net.sharksystem.api.interfaces;

import net.sharkfw.knowledgeBase.SharkKBException;

import java.io.InputStream;
import java.sql.Timestamp;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents the comments belonging to feeds
 */
public interface Comment extends Timeable, ContainsContent {

	/**
	 * Returns the author of a comment
	 * @return
     */
    Contact getSender() throws SharkKBException;

	/**
	 * Returns the Date and Time a comment was created
	 * @return
     */
    Timestamp getTimestamp() throws SharkKBException;

	/**
	 * Returns the Feed the comment is referencing
	 * @return
     */
    Feed getRefFeed() throws SharkKBException;

	void setContent(InputStream stream, String message, String mimeType) throws SharkKBException;

	/**
	 * returns the content of a comment
	 * @return
	 */
    Content getContent() throws SharkKBException;

	/**
	 * Deletes Comment from DB
	 */
	void delete() throws SharkKBException;

	/**
	 * marks the Comment as is disliked. Shark will collect dislikes and after an special amount it will inform the author
	 * @param isDisliked
	 */
	void setDisliked(boolean isDisliked) throws SharkKBException;

	/**
	 * Returns if the Comment is isDisliked
	 */
	boolean isDisliked() throws SharkKBException;

}

