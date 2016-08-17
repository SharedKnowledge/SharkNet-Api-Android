package net.sharksystem.api.dummy_impl;

import net.sharksystem.api.interfaces.Comment;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.api.interfaces.GetEvents;
import net.sharksystem.api.interfaces.Message;

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
