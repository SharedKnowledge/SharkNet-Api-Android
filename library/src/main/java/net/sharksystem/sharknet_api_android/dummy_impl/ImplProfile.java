package net.sharksystem.sharknet_api_android.dummy_impl;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.sharknet_api_android.interfaces.Blacklist;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Interest;
import net.sharksystem.sharknet_api_android.interfaces.Profile;
import net.sharksystem.sharknet_api_android.interfaces.Setting;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by timol on 16.05.2016.
 */
public class ImplProfile implements Profile {

	Contact c;
	String password ="";
	Setting setting;
	Blacklist blacklist;


	/**
	 * Constructor for new Profiles which are going to be saved
	 * @param nickname
     */
	public ImplProfile(String nickname, String deviceID){
		c = new ImplContact(nickname, gernateUID(nickname, deviceID), "", this);
		setting = new ImplSetting(this);
		save();
	}

	/**
	 * Constructor for the Database
	 * @param c
	 * @param password
	 * @param setting
	 * @param blacklist
     */
	public ImplProfile(Contact c, String password, Setting setting, Blacklist blacklist){
		this.c = c;
		this.password = password;
		this.setting = setting;
		this.blacklist = blacklist;
	}

	@Override
	public Setting getSettings() {
		if(setting == null) setting = new ImplSetting(this);
		return setting;
	}

	@Override
	public PeerSemanticTag getPST() {
		try {
			return c.getPST();
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getNickname() {
		try {
			return c.getNickname();
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setNickname(String nickname) {
		try {
			c.setNickname(nickname);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUID() {
		try {
			return c.getUID();
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setUID(String uid) {
		try {
			c.setUID(uid);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Content getPicture() {
		return null;
	}

	@Override
	public void setPicture(InputStream is, String title, String mimeType) throws IOException, SharkKBException {

	}

	@Override
	public String getPublicKey() {
		return null;
	}

	@Override
	public void setPublicKey(String publicKey) {

	}

	@Override
	public Timestamp getPublicKeyExpiration() {
		return null;
	}

	@Override
	public String getPublicKeyFingerprint() {
		return null;
	}

	@Override
	public void deleteKey() {

	}

	@Override
	public void delete() {

		//ToDo: Shark - delete the Profile in the KB
		//Implementation of DummyDB
		DummyDB.getInstance().removeProfile(this);
	}

	@Override
	public void save() {

		//ToDo: Shark - saveProfile in the KB
		//Implementation of DummyDB
		DummyDB.getInstance().addProfile(this);
	}

	@Override
	public void update() {
		//ToDo: Shark - update Profile in the KB
	}

	@Override
	public Interest getInterests() {
		return null;
	}

	@Override
	public boolean isEqual(Contact c) {
		return false;
	}

	@Override
	public Profile getOwner() {
		return null;
	}

	@Override
	public void addName(String name) {

	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void addTelephonnumber(String telephonnumber) {

	}

	@Override
	public List<String> getTelephonnumber() {
		return null;
	}

	@Override
	public void addNote(String note) {

	}

	@Override
	public String getNote() {
		return null;
	}

	@Override
	public void setEmail(String email) {
		try {
			c.setEmail(email);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getEmail() {
		try {
			return c.getEmail();
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Timestamp getLastWifiContact() {
		return null;
	}

	@Override
	public void setLastWifiContact(Timestamp lastWifiContact) {

	}

	@Override
	public boolean login(String password) {
		if(this.password.equals(password)){
			return true;
		}
		else return false;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public boolean isEqual(Profile p){
		if(p.isEqual(c)) return true;
		else return false;
	}

	@Override
	public Blacklist getBlacklist() {
		if(blacklist == null){
			blacklist = new ImplBlacklist(this);
		}
		return blacklist;
	}

	@Override
	public void renewKeys() {
		// Generate a new dummy public key

			//ToDo: Shark - Renew the keypair
	}

	private String gernateUID(String nickname, String deviceID){
		String newUID = "foo";
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss:SS");
		Date now = new Date(System.currentTimeMillis());
		String strDate = sdfDate.format(now);

		newUID = nickname + "/" + deviceID + "/" + strDate;
		return newUID;
	}


}
