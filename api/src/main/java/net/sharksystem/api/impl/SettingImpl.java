package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.Interest;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Setting;
import net.sharksystem.api.utils.ClassHelper;
import net.sharksystem.api.utils.SharkNetUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by j4rvis on 03.08.16.
 */
public class SettingImpl implements Setting {

    private static final String SETTING_NFC = "SETTING_NFC";
    private static final String SETTING_BLUETOOTH = "SETTING_BLUETOOTH";
    private static final String SETTING_TCP = "SETTING_TCP";
    private static final String SETTING_WIFI = "SETTING_WIFI";
    private static final String SETTING_MAIL = "SETTING_MAIL";
    private static final String SETTING_RADAR_ON = "SETTING_RADAR_ON";
    private static final String SETTING_WIFI_ON = "SETTING_WIFI_ON";
    private static final String SETTING_MAX_FILE_SIZE = "SETTING_MAX_FILE_SIZE";
    private static final String SETTING_SMTP_SERVER = "SETTING_SMTP_SERVER";
    private static final String SETTING_IMAP_SERVER = "SETTING_IMAP_SERVER";
    private static final String SETTING_SMTP_PORT = "SETTING_SMTP_PORT";
    private static final String SETTING_IMAP_PORT = "SETTING_IMAP_PORT";
    private static final String SETTING_IMAP_PASSWORD = "SETTING_IMAP_PASSWORD";
    private static final String SETTING_SMTP_PASSWORD = "SETTING_SMTP_PASSWORD";
    private static final String SETTING_EMAIL = "SETTING_EMAIL";
    private static final String SETTING_MAILBOX_SIZE = "SETTING_MAILBOX_SIZE";
    private static final String SETTING_SYNC_HOMEWORK = "SETTING_SYNC_HOMEWORK";
    private static final String SETTING_SYNC_TIMELINE = "SETTING_SYNC_TIMELINE";
    private static final String SETTING_SYNC_CHAT = "SETTING_SYNC_CHAT";
    private static final String SETTING_SYNC_CONTACT = "SETTING_SYNC_CONTACT";
    private static final String SETTING_SYNC_PROFILE = "SETTING_SYNC_PROFILE";
    private static final String SETTING_SYNC_NFC = "SETTING_SYNC_NFC";
    private static final String SETTING_SYNC_BLUETOOTH = "SETTING_SYNC_BLUETOOTH";
    private static final String SETTING_SYNC_TCP = "SETTING_SYNC_TCP";
    private static final String SETTING_SYNC_WIFI = "SETTING_SYNC_WIFI";
    private static final String SETTING_SYNC_MAIL = "SETTING_SYNC_MAIL";
    private static final String SETTING_ROUTING_CONTACTS = "SETTING_ROUTING_CONTACTS";
    private static final String SETTING_ROUTING_INTERESTS = "SETTING_ROUTING_INTERESTS";
    private static final String SETTING_ROUTING_FILE_SIZE = "SETTING_ROUTING_FILE_SIZE";

    private final SharkKB mKb;
    private final ASIPSpace mSpace;

    SettingImpl(SharkKB kb, ASIPSpace space){
        mKb = kb;
        mSpace = space;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        try {
            return ClassHelper.equals(Setting.class, this, o);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void save() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void syncData() {

    }

    @Override
    public void generateKeyPair() {

    }

    @Override
    public void startTCP() {

    }

    @Override
    public void stopTCP() {

    }

    @Override
    public void sendProfile(Contact sender, Contact recipient) {

    }

    @Override
    public boolean getNfc() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_NFC);
    }

    @Override
    public void setNfc(boolean nfc) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_NFC, nfc);
    }

    @Override
    public boolean getBluetooth() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_BLUETOOTH);
    }

    @Override
    public void setBluetooth(boolean bluetooth) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_BLUETOOTH, bluetooth);
    }

    @Override
    public boolean getTcp() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_TCP);
    }

    @Override
    public void setTcp(boolean tcp) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_TCP, tcp);
    }

    @Override
    public boolean getWifi() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_WIFI);
    }

    @Override
    public void setWifi(boolean wifi) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_WIFI, wifi);
    }

    @Override
    public boolean getMail() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_MAIL);
    }

    @Override
    public void setMail(boolean mail) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_MAIL, mail);
    }

    @Override
    public boolean getRadarON() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_RADAR_ON);
    }

    @Override
    public void setRadarON(boolean radarON) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_RADAR_ON, radarON);
    }

    @Override
    public boolean getWifiON() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_WIFI_ON);
    }

    @Override
    public void setWifiON(boolean wifiON) throws SharkKBException{
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_WIFI_ON, wifiON);
    }

    @Override
    public int getMaxFileSize() throws SharkKBException {
        return SharkNetUtils.getInfoAsInteger(mKb, mSpace, SETTING_MAX_FILE_SIZE);
    }

    @Override
    public void setMaxFileSize(int maxFileSize) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_MAX_FILE_SIZE, maxFileSize);
    }

    @Override
    public String getSmtpServer() throws SharkKBException {
        return SharkNetUtils.getInfoAsString(mKb, mSpace, SETTING_SMTP_SERVER);
    }

    @Override
    public void setSmtpServer(String smtpServer) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SMTP_SERVER, smtpServer);
    }

    @Override
    public String getImapServer() throws SharkKBException {
        return SharkNetUtils.getInfoAsString(mKb, mSpace, SETTING_IMAP_SERVER);
    }

    @Override
    public void setImapServer(String imapServer) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_IMAP_SERVER, imapServer);
    }

    @Override
    public int getSmtpPort() throws SharkKBException {
        return SharkNetUtils.getInfoAsInteger(mKb, mSpace, SETTING_SMTP_PORT);
    }

    @Override
    public void setSmtpPort(int portnumber) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SMTP_PORT, portnumber);
    }

    @Override
    public int getImapPort() throws SharkKBException {
        return SharkNetUtils.getInfoAsInteger(mKb, mSpace, SETTING_IMAP_PORT);
    }

    @Override
    public void setImapPort(int portnumber) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_IMAP_PORT, portnumber);
    }

    @Override
    public String getSmtpPassword() throws SharkKBException {
        return SharkNetUtils.getInfoAsString(mKb, mSpace, SETTING_SMTP_PASSWORD);
    }

    @Override
    public void setSmtpPassword(String smtpPassword) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SMTP_PASSWORD, smtpPassword);
    }

    @Override
    public String getImapPassword() throws SharkKBException {
        return SharkNetUtils.getInfoAsString(mKb, mSpace, SETTING_IMAP_PASSWORD);
    }

    @Override
    public void setImapPassword(String imapPassword) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_IMAP_PASSWORD, imapPassword);
    }

    @Override
    public int getMailboxSize() throws SharkKBException {
        return SharkNetUtils.getInfoAsInteger(mKb, mSpace, SETTING_MAILBOX_SIZE);
    }

    @Override
    public void setMailboxSize(int mailboxSize) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_MAILBOX_SIZE, mailboxSize);
    }

    @Override
    public String getEmail() throws SharkKBException {
        return SharkNetUtils.getInfoAsString(mKb, mSpace, SETTING_EMAIL);
    }

    @Override
    public void setEmail(String email) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_EMAIL, email);
    }

    @Override
    public boolean isSyncHausaufgaben() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_SYNC_HOMEWORK);
    }

    @Override
    public void setSyncHausaufgaben(boolean syncHausaufgaben) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SYNC_HOMEWORK, syncHausaufgaben);
    }

    @Override
    public boolean isSyncTimeline() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_SYNC_TIMELINE);
    }

    @Override
    public void setSyncTimeline(boolean syncTimeline) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SYNC_TIMELINE, syncTimeline);
    }

    @Override
    public boolean isSyncChat() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_SYNC_CHAT);
    }

    @Override
    public void setSyncChat(boolean syncChat) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SYNC_CHAT, syncChat);
    }

    @Override
    public boolean isSyncContact() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_SYNC_CONTACT);
    }

    @Override
    public void setSyncContact(boolean syncContact) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SYNC_CONTACT, syncContact);
    }

    @Override
    public boolean isSyncProfile() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_SYNC_PROFILE);
    }

    @Override
    public void setSyncProfile(boolean syncProfile) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SYNC_PROFILE, syncProfile);
    }

    @Override
    public boolean isSyncNfc() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_SYNC_NFC);
    }

    @Override
    public void setSyncNfc(boolean syncNfc) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SYNC_NFC, syncNfc);
    }

    @Override
    public boolean isSyncBluetooth() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_SYNC_BLUETOOTH);
    }

    @Override
    public void setSyncBluetooth(boolean syncBluetooth) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SYNC_BLUETOOTH, syncBluetooth);
    }

    @Override
    public boolean isSyncTcp() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_SYNC_NFC);
    }

    @Override
    public void setSyncTcp(boolean syncTcp) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SYNC_TCP, syncTcp);
    }

    @Override
    public boolean isSyncWifi() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_SYNC_WIFI);
    }

    @Override
    public void setSyncWifi(boolean syncWifi) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SYNC_WIFI, syncWifi);
    }

    @Override
    public boolean isSyncMail() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mSpace, SETTING_SYNC_MAIL);
    }

    @Override
    public void setSyncMail(boolean syncMail) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_SYNC_MAIL, syncMail);
    }

    @Override
    public int getRoutingFileSize() throws SharkKBException {
        return SharkNetUtils.getInfoAsInteger(mKb, mSpace, SETTING_ROUTING_FILE_SIZE);
    }

    @Override
    public void setRoutingFileSize(int routingFileSize) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mKb, mSpace, SETTING_ROUTING_FILE_SIZE, routingFileSize);
    }

    @Override
    public void addRoutingContacts(List<Contact> routingContacts) {

    }

    @Override
    public void addRoutingInterests(List<Interest> routingInterests) {

    }

    @Override
    public void deleteRoutingContacts(List<Contact> routingContacts) {

    }

    @Override
    public void deleteRoutingInterests(List<Interest> routingInterests) {

    }

    @Override
    public List<Interest> getRoutingInterests() {
        return null;
    }

    @Override
    public List<Contact> getRoutingContacts() {
        return null;
    }
}
