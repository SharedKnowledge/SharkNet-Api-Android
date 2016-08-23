package net.sharksystem.api.impl;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.SystemPropertyHolder;
import net.sharksystem.api.interfaces.Contact;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by j4rvis on 14.08.16.
 */
public class ContactTest extends BaseTest {

    @Test
    public void createContact_ContactIsInGetContacts() throws SharkKBException {
        Contact contact = mSharkNet.newContact(aliceName, aliceSI);

        contact.setEmail("alice@shark.de");
        contact.setNickname("Ali");
        contact.setLastWifiContact(new Timestamp(System.currentTimeMillis()-60*1000*5));
        contact.addNote("This is just a simple note.");
        contact.addTelephoneNumber("030123456789");
        contact.addTelephoneNumber("0177123456789");

        List<Contact> contacts = mSharkNet.getContacts();
        Contact next = contacts.iterator().next();

        Assert.assertTrue(contact.equals(next));

        mSharkNet.clearData();
    }
}
