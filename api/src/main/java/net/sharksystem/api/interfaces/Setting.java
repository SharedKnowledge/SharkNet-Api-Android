package net.sharksystem.api.interfaces;

import net.sharkfw.knowledgeBase.Interest;
import net.sharkfw.knowledgeBase.SharkKBException;

import java.io.IOException;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 */
public interface Setting {

	/**
	 * Returns if NFC is enabled
	 * @return
     */
	boolean getNfc() throws SharkKBException;

	/**
	 * Enable or Disable DataExchange via NFC
	 * @param nfc
     */
	void setNfc(boolean nfc) throws SharkKBException;

	/**
	 * Returns if Bluetooth is enabled
	 * @return
     */
	boolean getBluetooth() throws SharkKBException;

	/**
	 * Enable or Disable DataExchange via Bluetooth
	 * @param bluetooth
     */
	void setBluetooth(boolean bluetooth) throws SharkKBException;

	/**
	 * Returns if TCP is enabled
	 * @return
     */
	boolean getTcp() throws SharkKBException;

	/**
	 * Enable or Disable DataExchange via TCP
	 * @param tcp
     */
	void setTcp(boolean tcp) throws SharkKBException;

	/**
	 * Returns if Wifi is enabled
	 * @return
     */
	boolean getWifi() throws SharkKBException;

	/**
	 * Enable or Disable DataExchange via Wifi
	 * @param wifi
     */

	void setWifi(boolean wifi) throws SharkKBException;

	/**
	 * Returns if Mail for Dataexchange is enabled
	 * @return
     */
	boolean getMail() throws SharkKBException;

	/**
	 * Enable or Disable DataExchange via Mail
	 * @param mail
     */
	void setMail(boolean mail) throws SharkKBException;

	/**
	 * Returns if the Radar-Functionality is enabled
	 * @return
     */
	boolean getRadarON() throws SharkKBException;


	/**
	 * Enable or Disable Radar Functionality
	 * @param radarON
     */
	void setRadarON(boolean radarON) throws SharkKBException;

	/**
	 * Returns the time Wifi should be enabled before it`s going to be disabled
	 * @return
     */
	boolean getWifiON() throws SharkKBException;

	/**
	 * Sets the time Wifi should be enabled before it`s going to be disabled
	 * @param wifiON
     */
	void setWifiON(boolean wifiON) throws SharkKBException, IOException;

	/**
	 * Returns maximal file size which is going to be sent or received
	 * @return
     */
	int getMaxFileSize() throws SharkKBException;

	/**
	 * Sets maximal file size which is going to be sent or received
	 * @param maxFileSize
     */
	void setMaxFileSize(int maxFileSize) throws SharkKBException;

	/**
	 * Returns SMTP-Server for Exchange Data per Mail
	 * @return
     */
	String getSmtpServer() throws SharkKBException;

	/**
	 * Sets SMTP-Server for Exchange Data per Mail
	 * @param smtpServer
     */
	void setSmtpServer(String smtpServer) throws SharkKBException;

	/**
	 * Returns IMAP-Server for Exchange Data per Mail
	 * @return
     */
	String getImapServer() throws SharkKBException;

	/**
	 * Sets IMAP-Server for Exchange Data per Mail
	 * @param imapServer
     */
	void setImapServer(String imapServer) throws SharkKBException;

	/**
	 * Returns SMTP-Server-Port for Exchange Data per Mail
	 * @return
     */
	int getSmtpPort() throws SharkKBException;

	/**
	 * Returns IMAP-Server-Port for Exchange Data per Mail
	 * @return
     */
    int getImapPort() throws SharkKBException;

	/**
	 * Sets SMTP-Server-Port for Exchange Data per Mail
	 * @param portNumber
     */
    void setSmtpPort(int portNumber) throws SharkKBException;

	/**
	 * Sets IMAP-Server-Port for Exchange Data per Mail
	 * @param portNumber
     */
    void setImapPort(int portNumber) throws SharkKBException;


	/**
	 * Save Settings in Database
	 */
	void save();

	/**
	 * Deletes Settings in Database
	 */
	void delete();

	/**
	 * Syncs Data through configured way
	 */
	void syncData();

	/**
	 * Generates a new Pair of Keys and deletes the old ones
	 */
	void generateKeyPair();

	/**
	 * Returns Email-Adress which is configured for Data-Exchange
	 * @return
     */
	String getImapPassword() throws SharkKBException;

	/**
	 * Sets Email-Adress which is configured for Data-Exchange
	 * @param imapPassword
     */
	void setImapPassword(String imapPassword) throws SharkKBException;

	/**
	 * Returns Password of Email-Adress which is configured for Data-Exchange
	 * @return
     */
	String getEmail() throws SharkKBException;

	/**
	 * Returns the Password for the smtp Server
	 * @return
     */
	String getSmtpPassword() throws SharkKBException;

	/**
	 * Returns the Password for the smtp Server
	 * @param smtpPassword
     */
	void setSmtpPassword(String smtpPassword) throws SharkKBException;

	/**
	 * Returns the size of the Mailbox used for Dataexchange
	 * @return
     */
	int getMailboxSize() throws SharkKBException;

	/**
	 * Sets the size of the Mailbox used for dataexchange
	 * @param mailboxSize
     */
	void setMailboxSize(int mailboxSize) throws SharkKBException;


	/**
	 * Sets Password of Email-Adress which is configured for Data-Exchange
	 * @param email
     */
	void setEmail(String email) throws SharkKBException;


	/**
	 * Returns of synchronization of Hausaufgaben is enabled
	 * @return
     */
	boolean isSyncHausaufgaben() throws SharkKBException;

	/**
	 * Enable or disable synchronization of Hausaufgaben
	 * @param syncHausaufgaben
     */
	void setSyncHausaufgaben(boolean syncHausaufgaben) throws SharkKBException;

	/**
	 * Returns of synchronization of Timeline is enabled
 	 * @return
     */
    boolean isSyncTimeline() throws SharkKBException;

	/**
	 * Enable or disable synchronization of Timeline
	 * @param syncTimeline
     */
	void setSyncTimeline(boolean syncTimeline) throws SharkKBException;

	/**
	 * Returns of synchronization of Chat is enabled
	 * @return
     */
	boolean isSyncChat() throws SharkKBException;

	/**
	 * Enable or disable synchronization of Chat
	 * @param syncChat
     */
	void setSyncChat(boolean syncChat) throws SharkKBException;

	/**
	 * Returns of synchronization of Contacts is enabled
	 * @return
     */
	boolean isSyncContact() throws SharkKBException;

	/**
	 * Enable or disable synchronization of Contacts
	 * @param syncContact
     */
	void setSyncContact(boolean syncContact) throws SharkKBException;

	/**
	 * Returns of synchronization of Profile is enabled
	 * @return
     */
	boolean isSyncProfile() throws SharkKBException;

	/**
	 * Enable or disable synchronization of Profile
	 * @param syncProfile
     */

	void setSyncProfile(boolean syncProfile) throws SharkKBException;


	/**
	 * Returns if Sync per NFC is enabled
	 * @return
     */
	boolean isSyncNfc() throws SharkKBException;

	/**
	 * Enables or Disables Sync per NFC
	 * @param syncNfc
     */
	void setSyncNfc(boolean syncNfc) throws SharkKBException;
	/**
	 * Returns if Sync per Bluetooth is enabled
	 * @return
	 */
	boolean isSyncBluetooth() throws SharkKBException;

	/**
	 * Enables or Disables Sync per Bluetooth
	 * @param syncBluetooth
	 */
	void setSyncBluetooth(boolean syncBluetooth) throws SharkKBException;
	/**
	 * Returns if Sync per TCP is enabled
	 * @return
	 */
	boolean isSyncTcp() throws SharkKBException;

	/**
	 * Enables or Disables Sync per TCP
	 * @param syncTcp
	 */
	void setSyncTcp(boolean syncTcp) throws SharkKBException;
	/**
	 * Returns if Sync per Wifi is enabled
	 * @return
	 */
	boolean isSyncWifi() throws SharkKBException;

	/**
	 * Enables or Disables Sync per Wifi
	 * @param syncWifi
	 */
	void setSyncWifi(boolean syncWifi) throws SharkKBException;
	/**
	 * Returns if Sync per Mail is enabled
	 * @return
	 */
	boolean isSyncMail() throws SharkKBException;

	/**
	 * Enables or Disables Sync per Mail
	 * @param syncMail
	 */
	void setSyncMail(boolean syncMail) throws SharkKBException;

	/**
	 * start a TCP Server on the Client
	 */
	void startTCP();

	/**
	 * Stops the TCP-Server on the Client
	 */
	void stopTCP();

	/**
	 * Sends Contact of the Sender per TCP-Server (must be started) to the recipient
	 * @param sender
	 * @param recipient
     */
	void sendProfile(Contact sender, Contact recipient);

	/**
	 * Adds the Contacts to the allowed Routing List
	 * @param routingContacts
     */
	void addRoutingContacts(List<Contact> routingContacts);

	/**
	 * Adds the Interests to the allowed Routing List
	 * @param routingInterests
     */
	void addRoutingInterests(List<Interest> routingInterests);

	/**
	 * Removes the Contacts from the allowed Routing List
	 * @param routingContacts
     */
	void deleteRoutingContacts(List<Contact> routingContacts);

	/**
	 * Removes the Interests from the allowed Routing List
	 * @param routingInterests
     */
	void deleteRoutingInterests(List<Interest> routingInterests);

	/**
	 * Returns the Contacts of the allowed Routing List
	 * @return
     */
	List<Interest> getRoutingInterests();

	/**
	 * Returns the Interests of the allowed Routing List
	 * @return
     */
	List<Contact> getRoutingContacts();

	/**
	 * Sets the maximal File Size of Routing Data
	 * @param routingFileSize
     */
	void setRoutingFileSize(int routingFileSize) throws SharkKBException;

	/**
	 * Returns the maximal File Size of Routing Data
	 * @return
     */
	int getRoutingFileSize() throws SharkKBException;

}
