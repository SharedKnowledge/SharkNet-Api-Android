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
	 * Add a File to the Content, returns true if the Inputstream got saved, false if the Inputstream was bigger than maxfilesize
	 * @param f
	 * @return
	 */
//	public boolean setFile(File f);

	/**
	 * Add a File as Inputstream to the Content, returns true if the Inputstream got saved, false if the Inputstream was bigger than maxfilesize
	 * @param is
	 */
	public boolean setInputStream(InputStream is) throws IOException, SharkKBException;

	/**
	 * Returns a Outputstream of the File
	 * @return
	 */
	public OutputStream getOutputStream() throws SharkKBException;

	/**
	 * Returns the Filetype as Mime
	 * @return
	 */
	public String getMimeType() throws SharkKBException;

	/**
	 * Sets the Filetype (must be mime)
	 * @param mimeType
	 */
	public void setMimeType(String mimeType) throws SharkKBException;

	/**
	 *Returns the File as Inputstream
	 */
	public InputStream getInputStream() throws SharkKBException;

	/**
	 * Returns the Message as String
	 * @return
	 */
	public String getMessage() throws SharkKBException;

	/**
	 * Returns Filename as String
	 * @return
	 */
//	public String getFileName();

	/**
	 * Setter for the Filename
	 * @param filename
	 */
//	public void setFilename(String filename);
	/**
	 * Setter for the Message
	 */
	public void setMessage(String message) throws SharkKBException;

	/**
	 * Adds a Voting to the Content if not already done and returns the Object
	 * @param question
	 * @param singleqoice
	 * @return
	 */
	public ImplVoting addVoting(String question, boolean singleqoice);

	/**
	 * Returns the Voting, if it`s not there returns null
	 * @return
	 */
	public ImplVoting getVoting();

	/**
	 * Returns the InformationFile which is Part of the Sharkframework
	 * @return
	 */
//	public InMemoInformation getInformationFile();

	/**
	 * Sets the InformationFile which is part of the Sharkframework
	 * @param file
	 */
//	public void setInformationFile(InMemoInformation file);

	/**
	 * Getter for a Reminder (Bachelor Arbeit)
	 * @return
	 */
	public Reminder getReminder();

	/**
	 * Setter for Reminder (Bechelor Arbeit)
	 * @param reminder
	 */
	public void setReminder(Reminder reminder);

	public int getLength();

}