package net.sharksystem.api.dao;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.ContactDao;
import net.sharksystem.api.dao_impl.MessageDao;
import net.sharksystem.api.dao_impl.SharkNetApi;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by j4rvis on 3/26/17.
 */

public class MessageDaoTest {

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

    private MessageDao dao;
    private ContactDao contactDao;

    @Before
    public void setUp() throws Exception {
        L.setLogLevel(L.LOGLEVEL_ALL);
        contactDao = SharkNetApi.getInstance().getContactDao();
        dao = new MessageDao(new InMemoSharkKB());
    }

    @Test
    public void addMessageTest(){
        Contact contact = new Contact(aliceTag);
        contactDao.add(contact);
        Message message = new Message(contact);
        message.setContent("Das ist ein Test!");
        dao.add(message);
        Message savedMessage = dao.get(message.getId());
        Assert.assertTrue(savedMessage.equals(message));
    }

    @Test
    public void getContactTest_success() throws InterruptedException {
        Contact contact = new Contact(aliceTag);
        contactDao.add(contact);

        Message message1 = new Message(contact);
        message1.setContent("Test");
        dao.add(message1);
        Thread.sleep(2);
        Message message2 = new Message(contact);
        message2.setContent("TestTest");
        dao.add(message2);
        Thread.sleep(2);
        Message message3 = new Message(contact);
        message3.setContent("TestTestTest");
        dao.add(message3);

        Message saveMessage = dao.get(message1.getId());

        Assert.assertTrue(saveMessage.equals(message1));
    }


    @Test
    public void getContactTest_fail() throws InterruptedException {
        Contact contact = new Contact(aliceTag);
        contactDao.add(contact);

        Message message1 = new Message(contact);
        message1.setContent("Test");
        dao.add(message1);
        Thread.sleep(2);
        Message message2 = new Message(contact);
        message2.setContent("TestTest");
        dao.add(message2);
        Thread.sleep(2);
        Message message3 = new Message(contact);
        message3.setContent("TestTestTest");
        dao.add(message3);

        Message saveMessage = dao.get(message1.getId());

        Assert.assertFalse(saveMessage.equals(message2));
    }

    @Test
    public void getAllMessagesTest() throws InterruptedException {
        Contact contact = new Contact(aliceTag);
        contactDao.add(contact);
        Message message1 = new Message(contact);
        message1.setContent("Test");
        dao.add(message1);
        Thread.sleep(2);
        Message message2 = new Message(contact);
        message2.setContent("TestTest");
        dao.add(message2);
        Thread.sleep(2);
        Message message3 = new Message(contact);
        message3.setContent("TestTestTest");
        dao.add(message3);
        Assert.assertEquals(3, dao.size());
    }

    @Test
    public void updateMessageTest(){
        Contact contact = new Contact(aliceTag);
        contactDao.add(contact);
        Message message = new Message(contact);
        message.setContent("Test");
        dao.add(message);
        Message savedMessage1 = dao.get(message.getId());
        message.setSigned(true);
        message.setVerified(true);
        message.setEncrypted(true);
        message.setContent("Another test");
        dao.update(message);
        Message savedMessage2 = dao.get(message.getId());
        Assert.assertTrue(savedMessage2.equals(message));
        Assert.assertFalse(savedMessage1.equals(message));
    }

    @Test
    public void removeMessageTest() throws InterruptedException {
        Contact contact = new Contact(aliceTag);
        contactDao.add(contact);
        Message message1 = new Message(contact);
        message1.setContent("Test");
        dao.add(message1);
        Thread.sleep(2);
        Message message2 = new Message(contact);
        message2.setContent("TestTest");
        dao.add(message2);
        Thread.sleep(2);
        Message message3 = new Message(contact);
        message3.setContent("TestTestTest");
        dao.add(message3);
        Assert.assertEquals(3, dao.size());
        dao.remove(message1);
        Assert.assertEquals(2, dao.size());
    }
}
