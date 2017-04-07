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

    private ContactDao dao;

    @Before
    public void setUp() throws Exception {
        L.setLogLevel(L.LOGLEVEL_ALL);
    }

    @Test
    public void addContactTest(){
        dao = new ContactDao(new InMemoSharkKB());
        Contact contact = new Contact(aliceTag);
        dao.add(contact);
        Contact savedContact = dao.get(contact.getTag());
        Assert.assertTrue(savedContact.equals(contact));
    }

    @Test
    public void getContactTest(){
        dao = new ContactDao(new InMemoSharkKB());
        Contact contact = new Contact(aliceTag);
        dao.add(contact);
        dao.add(new Contact(bobTag));
        dao.add(new Contact(charlieTag));
        Contact savedContact = dao.get(contact.getTag());
        Assert.assertTrue(savedContact.equals(contact));
    }

    @Test
    public void getAllContactTest(){
        dao = new ContactDao(new InMemoSharkKB());
        dao.add(new Contact(aliceTag));
        dao.add(new Contact(bobTag));
        dao.add(new Contact(charlieTag));
        Assert.assertEquals(3, dao.size());
    }

    @Test
    public void updateContactTest(){
        dao = new ContactDao(new InMemoSharkKB());
        Contact contact = new Contact(aliceTag);
        dao.add(contact);
        contact.setName("Lilly");
        dao.update(contact);
        contact.setEmail("mail@lilly.com");
        dao.update(contact);
        Contact savedContact = dao.get(contact.getTag());
        Assert.assertTrue(savedContact.equals(contact));
    }

    @Test
    public void removeContactTest(){
        dao = new ContactDao(new InMemoSharkKB());
        Contact contact = new Contact(aliceTag);
        dao.add(contact);
        dao.add(new Contact(bobTag));
        dao.add(new Contact(charlieTag));
        Assert.assertEquals(3, dao.size());
        dao.remove(contact);
        Assert.assertEquals(2, dao.size());
    }
}
