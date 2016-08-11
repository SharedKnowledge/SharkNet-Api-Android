package net.sharksystem.sharknet_api_android.impl;

import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Interest;
import net.sharksystem.sharknet_api_android.interfaces.Setting;

import java.util.List;

/**
 * Created by j4rvis on 03.08.16.
 */
public class SettingImpl implements Setting {
    @Override
    public boolean getNfc() {
        return false;
    }

    @Override
    public void setNfc(boolean nfc) {

    }

    @Override
    public boolean getBluetooth() {
        return false;
    }

    @Override
    public void setBluetooth(boolean bluetooth) {

    }

    @Override
    public boolean getTcp() {
        return false;
    }

    @Override
    public void setTcp(boolean tcp) {

    }

    @Override
    public boolean getWifi() {
        return false;
    }

    @Override
    public void setWifi(boolean wifi) {

    }

    @Override
    public boolean getMail() {
        return false;
    }

    @Override
    public void setMail(boolean mail) {

    }

    @Override
    public boolean getRadarON() {
        return false;
    }

    @Override
    public void setRadarON(boolean radarON) {

    }

    @Override
    public int getWifiON() {
        return 0;
    }

    @Override
    public void setWifiON(int wifiON) {

    }

    @Override
    public int getMaxFileSize() {
        return 0;
    }

    @Override
    public void setMaxFileSize(int maxFileSize) {

    }

    @Override
    public String getSmtpServer() {
        return null;
    }

    @Override
    public void setSmtpServer(String smtpServer) {

    }

    @Override
    public String getImapServer() {
        return null;
    }

    @Override
    public void setImapServer(String imapServer) {

    }

    @Override
    public int getSmtpPort() {
        return 0;
    }

    @Override
    public int getImapPort() {
        return 0;
    }

    @Override
    public void setSmtpPort(int portnumber) {

    }

    @Override
    public void setImapPort(int portnumber) {

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
    public String getImapPassword() {
        return null;
    }

    @Override
    public void setImapPassword(String imapPassword) {

    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getSmtpPassword() {
        return null;
    }

    @Override
    public void setSmtpPassword(String smtpPassword) {

    }

    @Override
    public int getMailboxSize() {
        return 0;
    }

    @Override
    public void setMailboxSize(int mailboxSize) {

    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public boolean isSyncHausaufgaben() {
        return false;
    }

    @Override
    public void setSyncHausaufgaben(boolean syncHausaufgaben) {

    }

    @Override
    public boolean isSyncTimeline() {
        return false;
    }

    @Override
    public void setSyncTimeline(boolean syncTimeline) {

    }

    @Override
    public boolean isSyncChat() {
        return false;
    }

    @Override
    public void setSyncChat(boolean syncChat) {

    }

    @Override
    public boolean isSyncContact() {
        return false;
    }

    @Override
    public void setSyncContact(boolean syncContact) {

    }

    @Override
    public boolean isSyncProfile() {
        return false;
    }

    @Override
    public void setSyncProfile(boolean syncProfile) {

    }

    @Override
    public boolean isSyncNfc() {
        return false;
    }

    @Override
    public void setSyncNfc(boolean syncNfc) {

    }

    @Override
    public boolean isSyncBluetooth() {
        return false;
    }

    @Override
    public void setSyncBluetooth(boolean syncBluetooth) {

    }

    @Override
    public boolean isSyncTcp() {
        return false;
    }

    @Override
    public void setSyncTcp(boolean syncTcp) {

    }

    @Override
    public boolean isSyncWifi() {
        return false;
    }

    @Override
    public void setSyncWifi(boolean syncWifi) {

    }

    @Override
    public boolean isSyncMail() {
        return false;
    }

    @Override
    public void setSyncMail(boolean syncMail) {

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

    @Override
    public void setRoutingFileSize(int routingFileSize) {

    }

    @Override
    public int getRoutingFileSize() {
        return 0;
    }
}
