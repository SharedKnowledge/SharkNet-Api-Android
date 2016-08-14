package net.sharksystem.sharknet_api_android.interfaces;

import net.sharkfw.knowledgeBase.Interest;

import java.util.List;

/**
 * Created by timol on 12.05.2016.
 */
public interface Setting {

	/**
	 * Returns if NFC is enabled
	 * @return
     */
	boolean getNfc();

	/**
	 * Enable or Disable DataExchange via NFC
	 * @param nfc
     */
	void setNfc(boolean nfc);

	/**
	 * Returns if Bluetooth is enabled
	 * @return
     */
	boolean getBluetooth();

	/**
	 * Enable or Disable DataExchange via Bluetooth
	 * @param bluetooth
     */
	void setBluetooth(boolean bluetooth);

	/**
	 * Returns if TCP is enabled
	 * @return
     */
	boolean getTcp();

	/**
	 * Enable or Disable DataExchange via TCP
	 * @param tcp
     */
	void setTcp(boolean tcp);

	/**
	 * Returns if Wifi is enabled
	 * @return
     */
	boolean getWifi();

	/**
	 * Enable or Disable DataExchange via Wifi
	 * @param wifi
     */

	void setWifi(boolean wifi);

	/**
	 * Returns if Mail for Dataexchange is enabled
	 * @return
     */
	boolean getMail();

	/**
	 * Enable or Disable DataExchange via Mail
	 * @param mail
     */
	void setMail(boolean mail);

	/**
	 * Returns if the Radar-Functionality is enabled
	 * @return
     */
	boolean getRadarON();


	/**
	 * Enable or Disable Radar Functionality
	 * @param radarON
     */
	void setRadarON(boolean radarON);

	/**
	 * Returns the time Wifi should be enabled before it`s going to be disabled
	 * @return
     */
	int getWifiON();

	/**
	 * Sets the time Wifi should be enabled before it`s going to be disabled
	 * @param wifiON
     */
	void setWifiON(int wifiON);

	/**
	 * Returns maximal file size which is going to be sent or received
	 * @return
     */
	int getMaxFileSize();

	/**
	 * Sets maximal file size which is going to be sent or received
	 * @param maxFileSize
     */
	void setMaxFileSize(int maxFileSize);

	/**
	 * Returns SMTP-Server for Exchange Data per Mail
	 * @return
     */
	String getSmtpServer();

	/**
	 * Sets SMTP-Server for Exchange Data per Mail
	 * @param smtpServer
     */
	void setSmtpServer(String smtpServer);

	/**
	 * Returns IMAP-Server for Exchange Data per Mail
	 * @return
     */
	String getImapServer();

	/**
	 * Sets IMAP-Server for Exchange Data per Mail
	 * @param imapServer
     */
	void setImapServer(String imapServer);

	/**
	 * Returns SMTP-Server-Port for Exchange Data per Mail
	 * @return
     */
	int getSmtpPort();

	/**
	 * Returns IMAP-Server-Port for Exchange Data per Mail
	 * @return
     */
    int getImapPort();

	/**
	 * Sets SMTP-Server-Port for Exchange Data per Mail
	 * @param portNumber
     */
    void setSmtpPort(int portNumber);

	/**
	 * Sets IMAP-Server-Port for Exchange Data per Mail
	 * @param portNumber
     */
    void setImapPort(int portNumber);


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
	String getImapPassword();

	/**
	 * Sets Email-Adress which is configured for Data-Exchange
	 * @param imapPassword
     */
	void setImapPassword(String imapPassword);

	/**
	 * Returns Password of Email-Adress which is configured for Data-Exchange
	 * @return
     */
	String getEmail();

	/**
	 * Returns the Password for the smtp Server
	 * @return
     */
	String getSmtpPassword();

	/**
	 * Returns the Password for the smtp Server
	 * @param smtpPassword
     */
	void setSmtpPassword(String smtpPassword);

	/**
	 * Returns the size of the Mailbox used for Dataexchange
	 * @return
     */
	int getMailboxSize();

	/**
	 * Sets the size of the Mailbox used for dataexchange
	 * @param mailboxSize
     */
	void setMailboxSize(int mailboxSize);


	/**
	 * Sets Password of Email-Adress which is configured for Data-Exchange
	 * @param email
     */
	void setEmail(String email);


	/**
	 * Returns of synchronization of Hausaufgaben is enabled
	 * @return
     */
	boolean isSyncHausaufgaben();

	/**
	 * Enable or disable synchronization of Hausaufgaben
	 * @param syncHausaufgaben
     */
	void setSyncHausaufgaben(boolean syncHausaufgaben);

	/**
	 * Returns of synchronization of Timeline is enabled
 	 * @return
     */
    boolean isSyncTimeline();

	/**
	 * Enable or disable synchronization of Timeline
	 * @param syncTimeline
     */
	void setSyncTimeline(boolean syncTimeline);

	/**
	 * Returns of synchronization of Chat is enabled
	 * @return
     */
	boolean isSyncChat();

	/**
	 * Enable or disable synchronization of Chat
	 * @param syncChat
     */
	void setSyncChat(boolean syncChat);

	/**
	 * Returns of synchronization of Contacts is enabled
	 * @return
     */
	boolean isSyncContact();

	/**
	 * Enable or disable synchronization of Contacts
	 * @param syncContact
     */
	void setSyncContact(boolean syncContact);

	/**
	 * Returns of synchronization of Profile is enabled
	 * @return
     */
	boolean isSyncProfile() ;

	/**
	 * Enable or disable synchronization of Profile
	 * @param syncProfile
     */

	void setSyncProfile(boolean syncProfile);


	/**
	 * Returns if Sync per NFC is enabled
	 * @return
     */
	boolean isSyncNfc();

	/**
	 * Enables or Disables Sync per NFC
	 * @param syncNfc
     */
	void setSyncNfc(boolean syncNfc);
	/**
	 * Returns if Sync per Bluetooth is enabled
	 * @return
	 */
	boolean isSyncBluetooth();

	/**
	 * Enables or Disables Sync per Bluetooth
	 * @param syncBluetooth
	 */
	void setSyncBluetooth(boolean syncBluetooth);
	/**
	 * Returns if Sync per TCP is enabled
	 * @return
	 */
	boolean isSyncTcp();

	/**
	 * Enables or Disables Sync per TCP
	 * @param syncTcp
	 */
	void setSyncTcp(boolean syncTcp);
	/**
	 * Returns if Sync per Wifi is enabled
	 * @return
	 */
	boolean isSyncWifi();

	/**
	 * Enables or Disables Sync per Wifi
	 * @param syncWifi
	 */
	void setSyncWifi(boolean syncWifi);
	/**
	 * Returns if Sync per Mail is enabled
	 * @return
	 */
	boolean isSyncMail();

	/**
	 * Enables or Disables Sync per Mail
	 * @param syncMail
	 */
	void setSyncMail(boolean syncMail);

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
	void setRoutingFileSize(int routingFileSize);

	/**
	 * Returns the maximal File Size of Routing Data
	 * @return
     */
	int getRoutingFileSize();

}
