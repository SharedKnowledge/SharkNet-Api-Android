package net.sharksystem.sharknet_api_android.dummy_impl;

import net.sharksystem.sharknet_api_android.interfaces.Comment;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Feed;
import net.sharksystem.sharknet_api_android.interfaces.GetEvents;
import net.sharksystem.sharknet_api_android.interfaces.Message;

/**
 * @Author Yves Kaufmann
 * @since 09.07.2016
 */
public abstract class GetEventsAdapter implements GetEvents {
	@Override
	public void receivedMessage(Message m) {}

	@Override
	public void receivedFeed(Feed f) {}

	@Override
	public void receivedComment(Comment c) {

	}
	@Override
	public void receivedContact(Contact c) {

	}
}
