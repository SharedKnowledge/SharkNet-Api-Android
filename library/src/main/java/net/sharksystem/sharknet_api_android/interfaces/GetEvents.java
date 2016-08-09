package net.sharksystem.sharknet_api_android.interfaces;

/**
 * Created by timol on 27.06.2016.
 */


import net.sharkfw.knowledgeBase.SharkKBException;

/**
 * This Interface have to be implemented to receive Notifications when sth is received
 */
public interface GetEvents {
	public void receivedMessage(Message m) throws SharkKBException;
	public void receivedFeed(Feed f) throws SharkKBException;
	public void receivedComment(Comment c) throws SharkKBException;
	public void receivedContact(Contact c);

}
