package net.sharksystem.api.impl;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Contact;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by j4rvis on 14.08.16.
 */
public class ContactTest extends BaseTest {

    @Test
    public void createContact_ContactIsInGetContacts() throws SharkKBException {
        Contact contact = mSharkNet.newContact(aliceName, aliceSI);

        List<Contact> contacts = mSharkNet.getContacts();
        Contact next = contacts.iterator().next();

        Assert.assertTrue(contact.equals(next));
    }
}
