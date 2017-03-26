package net.sharksystem.api.dao;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.ContactDao;
import net.sharksystem.api.models.Contact;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by j4rvis on 3/26/17.
 */

public class ContactDaoTest {

    String aliceName = "Alice";
    String aliceSI = "www.facebook.com/alice";
    String aliceMail = "mail://alice.com";

    PeerSemanticTag aliceTag = InMemoSharkKB.createInMemoPeerSemanticTag(aliceName, aliceSI, aliceMail);

    String bobName = "Bob";
    String bobSI = "www.facebook.com/bob";
    String bobMail = "mail://bob.com";

    PeerSemanticTag bobTag = InMemoSharkKB.createInMemoPeerSemanticTag(bobName, bobSI, bobMail);

    String charlieName = "Charlie";
    String charlieSI = "www.facebook.com/charlie";
    String charlieMail = "mail://charlie.com";

    PeerSemanticTag charlieTag = InMemoSharkKB.createInMemoPeerSemanticTag(charlieName, charlieSI, charlieMail);

    private ContactDao mContactDao;

    @Before
    public void setUp() throws Exception {
        L.setLogLevel(L.LOGLEVEL_ALL);
        mContactDao = new ContactDao(new InMemoSharkKB());
    }

    @Test
    public void addContactTest(){
        Contact contact = new Contact(aliceTag);
        mContactDao.add(contact);
        Contact savedContact = mContactDao.get(contact.getTag());
        Assert.assertTrue(savedContact.equals(contact));
    }

    @Test
    public void getContactTest(){
        Contact contact = new Contact(aliceTag);
        mContactDao.add(contact);
        mContactDao.add(new Contact(bobTag));
        mContactDao.add(new Contact(charlieTag));
        Contact savedContact = mContactDao.get(contact.getTag());
        Assert.assertTrue(savedContact.equals(contact));
    }

    @Test
    public void updateContactTest(){
        Contact contact = new Contact(aliceTag);
        mContactDao.add(contact);
        contact.setName("Lilly");
        mContactDao.update(contact);
        contact.setEmail("mail@lilly.com");
        mContactDao.update(contact);
        Contact savedContact = mContactDao.get(contact.getTag());
        Assert.assertTrue(savedContact.equals(contact));
    }

    

}
