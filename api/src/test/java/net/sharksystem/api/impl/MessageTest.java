package net.sharksystem.api.impl;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Message;
import net.sharksystem.api.interfaces.Profile;
import net.sharksystem.api.utils.DummyCreator;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 24.08.16.
 */
public class MessageTest extends BaseTest {

    @Test
    public void checkIfMessageIsMine() throws SharkKBException, JSONException {
        Profile profile = mSharkNet.newProfile(aliceName, aliceSI);
        mSharkNet.setActiveProfile(profile, "");

        Contact contact = mSharkNet.newContact(bobName, bobSI);

        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(contact);

        Chat chat = mSharkNet.newChat(contacts);
        chat.sendMessage("A message");

        List<Message> messages = chat.getMessages(true);
        Message message = messages.get(0);

        Assert.assertTrue(message.isMine());
    }


    @Test
    public void checkIfMessageIsNOTMine() throws SharkKBException, JSONException {
        Profile profile = mSharkNet.newProfile(aliceName, aliceSI);
        mSharkNet.setActiveProfile(profile, "");

        Contact bob = mSharkNet.newContact(bobName, bobSI);

        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(bob);

        Chat chat = mSharkNet.newChat(contacts);
        chat.sendMessage("A message", bob);

        List<Message> messages = chat.getMessages(true);
        Message message = messages.get(0);

        Assert.assertTrue(!message.isMine());
    }
}
