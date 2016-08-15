package net.sharksystem.sharknet_api_android.interfaces;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents the Profile-Functionality, in our case the personal contact
 */
public interface Profile extends Contact{
	/**
	 * Returns the Settings of the App
	 * @return
     */
    Setting getSettings();

	/**
	 * Saves the Profile in the KB
	 */
	void save();

	void update();

	/**
	 * Method for the Login
	 */

	boolean login(String password);
	/**
	 * Method to set password
	 */
	void setPassword(String password);

	/**
	 * Returns true if profiles are equal
	 * @param p
	 * @return
     */
	boolean isEqual(Profile p);

	/**
	 * Returns the Blacklist of the Profile
	 * @return
     */
	Blacklist getBlacklist();

	/**
	 * Generates new pair of keys
	 */
	void renewKeys();
}
