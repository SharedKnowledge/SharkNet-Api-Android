package net.sharksystem.api.interfaces;

import net.sharkfw.knowledgeBase.SharkKBException;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by timol on 06.06.2016.
 */
public interface Timeable {
	/**
	 * The Interface is used by Messages, Feeds and Commands for easy sorting the lists
	 */

	Date getDateReceived() throws SharkKBException;
}
