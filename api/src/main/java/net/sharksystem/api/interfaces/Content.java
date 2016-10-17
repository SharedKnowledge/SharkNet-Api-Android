package net.sharksystem.api.interfaces;

import net.sharkfw.knowledgeBase.SharkKBException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by timol on 01.06.2016.
 */
public interface Content {

	/**
	 * Add InputStream to the Content, returns true if the InputStream got saved, false if the InputStream was bigger than maxfilesize
	 * @param is
	 */
	boolean setInputStream(InputStream is) throws IOException, SharkKBException;

	/**
	 * Returns the File type as Mime
	 * @return
	 */
	String getMimeType() throws SharkKBException;

	/**
	 * Sets the Filetype (must be mime)
	 * @param mimeType
	 */
	void setMimeType(String mimeType) throws SharkKBException;

	/**
	 *Returns the File as InputStream
	 */
	InputStream getInputStream() throws SharkKBException;

	/**
	 * Returns the Message as String
	 * @return
	 */
	String getMessage() throws SharkKBException;

	/**
	 * Setter for the Message
	 */
	void setMessage(String message) throws SharkKBException;

	long getLength() throws SharkKBException;

	/**
	 * Adds a Voting to the Content if not already done and returns the Object
	 * @param question
	 * @param singleqoice
	 * @return
	 */
	Voting addVoting(String question, boolean singleqoice);

	/**
	 * Returns the Voting, if it`s not there returns null
	 * @return
	 */
	Voting getVoting();
}