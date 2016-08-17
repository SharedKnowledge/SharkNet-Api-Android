package net.sharksystem.api.dummy_impl;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Comment;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.api.interfaces.GetEvents;
import net.sharksystem.api.interfaces.Message;

/**
 * Created by timol on 27.06.2016.
 */
public class TestListener  implements GetEvents {
	@Override
	public void receivedMessage(Message m) throws SharkKBException {
		System.out.println(m.getContent().getMessage());
	}

	@Override
	public void receivedFeed(Feed f) throws SharkKBException {
		System.out.println(f.getContent().getMessage());
	}

	@Override
	public void receivedComment(Comment c) throws SharkKBException {
		System.out.println(c.getContent().getMessage());
	}

	@Override
	public void receivedContact(Contact c) {
		try {
			System.out.println(c.getNickname());
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
	}
}
