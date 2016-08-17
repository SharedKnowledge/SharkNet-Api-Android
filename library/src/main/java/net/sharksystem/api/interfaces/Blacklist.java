package net.sharksystem.api.interfaces;

import java.util.List;

/**
 * Created by timol on 06.06.2016.
 */
public interface Blacklist {

	/**
	 * Add a Contact to the Blacklist
	 * @param c
     */
	void add(Contact c);

	/**
	 * Remove a Contact from the Blacklist
	 * @param c
     */
	void remove(Contact c);

	/**
	 * Get the List of Contacts in the Blacklist
	 * @return
     */
	List<Contact> getList();


}
