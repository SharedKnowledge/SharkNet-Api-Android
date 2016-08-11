package net.sharksystem.sharknet_api_android.interfaces;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoInformation;
import net.sharksystem.sharknet_api_android.dummy_impl.ImplVoting;
import net.sharksystem.sharknet_api_android.dummy_impl.Reminder;

import java.io.*;

/**
 * Created by timol on 01.06.2016.
 */
public interface Content {

	/**
	 * Add a File to the Content, returns true if the InputStream got saved, false if the InputStream was bigger than max files ize
	 * @param f
	 * @return
	 */
//	public boolean setFile(File f);

	/**
	 * Add a File as InputStream to the Content, returns true if the InputStream got saved, false if the InputStream was bigger than maxfilesize
	 * @param is
	 */
	boolean setInputStream(InputStream is) throws IOException, SharkKBException;

	/**
	 * Returns a OutputStream of the File
	 * @return
	 */
	OutputStream getOutputStream() throws SharkKBException;

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
	 * Returns Filename as String
	 * @return
	 */
//	String getFileName();

	/**
	 * Setter for the Filename
	 * @param filename
	 */
//	void setFilename(String filename);
	/**
	 * Setter for the Message
	 */
	void setMessage(String message) throws SharkKBException;

	/**
	 * Adds a Voting to the Content if not already done and returns the Object
	 * @param question
	 * @param singleqoice
	 * @return
	 */
	ImplVoting addVoting(String question, boolean singleqoice);

	/**
	 * Returns the Voting, if it`s not there returns null
	 * @return
	 */
	ImplVoting getVoting();

	/**
	 * Returns the InformationFile which is Part of the SharkFramework
	 * @return
	 */
//	InMemoInformation getInformationFile();

	/**
	 * Sets the InformationFile which is part of the SharkFramework
	 * @param file
	 */
//	void setInformationFile(InMemoInformation file);

	/**
	 * Getter for a Reminder (Bachelor Arbeit)
	 * @return
	 */
	Reminder getReminder();

	/**
	 * Setter for Reminder (Bechelor Arbeit)
	 * @param reminder
	 */
	void setReminder(Reminder reminder);

	int getLength();

}