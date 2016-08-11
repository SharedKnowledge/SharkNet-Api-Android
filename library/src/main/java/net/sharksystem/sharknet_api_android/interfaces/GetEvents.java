package net.sharksystem.sharknet_api_android.interfaces;

/**
 * Created by timol on 27.06.2016.
 */


import net.sharkfw.knowledgeBase.SharkKBException;

/**
 * This Interface have to be implemented to receive Notifications when sth is received
 */
public interface GetEvents {
	void receivedMessage(Message m) throws SharkKBException;
	void receivedFeed(Feed f) throws SharkKBException;
	void receivedComment(Comment c) throws SharkKBException;
	void receivedContact(Contact c);

}
