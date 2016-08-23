package net.sharksystem.api.impl;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Contact;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by j4rvis on 23.08.16.
 */
public class SharkNetEngineTest extends BaseTest {

    @Test
    public void contactCreationTest() throws SharkKBException {
        mSharkNet.newContact(aliceName, aliceSI, "");
        mSharkNet.newContact(bobName, bobSI, "");
        mSharkNet.newContact(charlieName, charlieSI, "");

        List<Contact> contacts = mSharkNet.getContacts();

        Assert.assertTrue(contacts.size() == 3);
    }

    @Test
    public void getContactByTag_success() throws SharkKBException {
        Contact alice = mSharkNet.newContact(aliceName, aliceSI, "");
        mSharkNet.newContact(bobName, bobSI, "");
        mSharkNet.newContact(charlieName, charlieSI, "");

        Contact byTag = mSharkNet.getContactByTag(alice.getPST());
        Assert.assertTrue(byTag.equals(alice));
    }


}
