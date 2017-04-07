package net.sharksystem.api.dao;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.ContactDao;
import net.sharksystem.api.dao_impl.MessageDao;
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

    private ContactDao contactDao;
    private Contact alice;
    private Contact bob;
    private Contact charlie;

    @Before
    public void setUp() throws Exception {
        L.setLogLevel(L.LOGLEVEL_ALL);
        contactDao = new ContactDao(new InMemoSharkKB());
        alice = new Contact(aliceTag);
        bob = new Contact(bobTag);
        charlie = new Contact(charlieTag);
        contactDao.add(alice);
        contactDao.add(bob);
        contactDao.add(charlie);
    }

    @Test
    public void addMessageTest(){
        MessageDao dao = new MessageDao(new InMemoSharkKB(), contactDao);
        Message message = new Message(alice);
        message.setContent("Das ist ein Test!");
        dao.add(message);
        Message savedMessage = dao.get(message.getId());
        Assert.assertTrue(savedMessage.equals(message));
    }

    @Test
    public void getContactTest_success() throws InterruptedException {
        MessageDao dao = new MessageDao(new InMemoSharkKB(), contactDao);
        Message message1 = new Message(alice);
        message1.setContent("Test");
        dao.add(message1);
        Thread.sleep(2);
        Message message2 = new Message(alice);
        message2.setContent("TestTest");
        dao.add(message2);
        Thread.sleep(2);
        Message message3 = new Message(alice);
        message3.setContent("TestTestTest");
        dao.add(message3);

        Message saveMessage = dao.get(message1.getId());

        Assert.assertTrue(saveMessage.equals(message1));
    }


    @Test
    public void getContactTest_fail() throws InterruptedException {
        MessageDao dao = new MessageDao(new InMemoSharkKB(), contactDao);

        Message message1 = new Message(alice);
        message1.setContent("Test");
        dao.add(message1);
        Thread.sleep(2);
        Message message2 = new Message(alice);
        message2.setContent("TestTest");
        dao.add(message2);
        Thread.sleep(2);
        Message message3 = new Message(alice);
        message3.setContent("TestTestTest");
        dao.add(message3);

        Message saveMessage = dao.get(message1.getId());

        Assert.assertFalse(saveMessage.equals(message2));
    }

    @Test
    public void getAllMessagesTest() throws InterruptedException {
        MessageDao dao = new MessageDao(new InMemoSharkKB(), contactDao);
        Message message1 = new Message(alice);
        message1.setContent("Test");
        dao.add(message1);
        Thread.sleep(2);
        Message message2 = new Message(alice);
        message2.setContent("TestTest");
        dao.add(message2);
        Thread.sleep(2);
        Message message3 = new Message(alice);
        message3.setContent("TestTestTest");
        dao.add(message3);
        Assert.assertEquals(3, dao.size());
    }

    @Test
    public void updateMessageTest(){
        MessageDao dao = new MessageDao(new InMemoSharkKB(), contactDao);
        Message message = new Message(alice);
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
        MessageDao dao = new MessageDao(new InMemoSharkKB(), contactDao);
        Message message1 = new Message(alice);
        message1.setContent("Test");
        dao.add(message1);
        Thread.sleep(2);
        Message message2 = new Message(alice);
        message2.setContent("TestTest");
        dao.add(message2);
        Thread.sleep(2);
        Message message3 = new Message(alice);
        message3.setContent("TestTestTest");
        dao.add(message3);
        Assert.assertEquals(3, dao.size());
        dao.remove(message1);
        Assert.assertEquals(2, dao.size());
    }
}
