package net.sharksystem.api.impl;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Message;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 23.08.16.
 */
public class ChatTest extends BaseTest {

    @Test
    public void createChat() throws SharkKBException, JSONException {
        Contact alice = mSharkNet.newContact(aliceName, aliceSI);
        Contact bob = mSharkNet.newContact(bobName, bobSI);

        Chat chat = mSharkNet.newChat(mSharkNet.getContacts());
        List<Contact> contacts = chat.getContacts();

        Assert.assertTrue(contacts.size() == 2);
    }

    @Test
    public void createMessageInChat() throws SharkKBException, JSONException {
        Contact alice = mSharkNet.newContact(aliceName, aliceSI);
        Contact bob = mSharkNet.newContact(bobName, bobSI);

        Chat chat = mSharkNet.newChat(mSharkNet.getContacts());
        chat.sendMessage(stream, "A message", "type");

        List<Message> messages = chat.getMessages(false);
        Message message = messages.get(0);
//        InputStream localStream = message.getContent().getInputStream();
        Assert.assertTrue(message.getContent().getMessage().equals("A message"));
//        Assert.assertEquals(localStream.hashCode(), stream.hashCode());
//        Assert.assertTrue(message.getContent().getMimeType().equals("type"));
    }
}
