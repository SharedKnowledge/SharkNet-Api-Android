package net.sharksystem.sharknet_api_android.dummy_impl;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.sharknet_api_android.interfaces.Comment;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Feed;
import net.sharksystem.sharknet_api_android.interfaces.GetEvents;
import net.sharksystem.sharknet_api_android.interfaces.Message;

/**
 * Created by timol on 27.06.2016.
 */
public class TestListener  implements GetEvents {
	@Override
	public void receivedMessage(Message m) {
		System.out.println(m.getContent().getMessage());
	}

	@Override
	public void receivedFeed(Feed f) {
		System.out.println(f.getContent().getMessage());
	}

	@Override
	public void receivedComment(Comment c) {
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
