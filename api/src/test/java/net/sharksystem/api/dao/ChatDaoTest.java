package net.sharksystem.api.dao;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.ChatDao;
import net.sharksystem.api.dao_impl.ContactDao;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 3/26/17.
 */

public class ChatDaoTest {

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
    public void addChatTest(){
        ChatDao dao = new ChatDao(new InMemoSharkKB(), contactDao);
        Chat chat = new Chat(alice, bob);
        chat.setTitle("Chat mit Bob");
        Message message = new Message(alice);
        message.setContent("Hallo Bob");
        chat.addMessage(message);
        dao.add(chat);
        Chat savedChat = dao.get(chat.getId());
        Assert.assertTrue(chat.equals(savedChat));
    }

    @Test
    public void getChatTest(){
        ChatDao dao = new ChatDao(new InMemoSharkKB(), contactDao);
        Chat chat1 = new Chat(alice, bob);
        chat1.setTitle("Chat mit Bob");
        Message message1 = new Message(alice);
        message1.setContent("Hallo Bob");
        chat1.addMessage(message1);
        dao.add(chat1);

        Chat chat2 = new Chat(alice, charlie);
        chat2.setTitle("Chat mit Charlie");
        Message message2 = new Message(charlie);
        message2.setContent("Hallo Alice");
        chat2.addMessage(message2);
        dao.add(chat2);

        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(bob);
        contacts.add(charlie);
        Chat chat3 = new Chat(alice, contacts);
        chat3.setTitle("Chat mit Bob und Charlie");
        Message message3 = new Message(charlie);
        message3.setContent("Hallo Alice");
        chat3.addMessage(message3);
        Message message4 = new Message(alice);
        message4.setContent("Hallo ihr zwei");
        chat3.addMessage(message4);
        dao.add(chat3);

        Chat savedChat = dao.get(chat2.getId());
        Assert.assertTrue(savedChat.equals(chat2));
    }

    @Test
    public void getSizeTest(){
        ChatDao dao = new ChatDao(new InMemoSharkKB(), contactDao);
        Chat chat1 = new Chat(alice, bob);
        chat1.setTitle("Chat mit Bob");
        Message message1 = new Message(alice);
        message1.setContent("Hallo Bob");
        chat1.addMessage(message1);
        dao.add(chat1);

        Chat chat2 = new Chat(alice, charlie);
        chat2.setTitle("Chat mit Charlie");
        Message message2 = new Message(charlie);
        message2.setContent("Hallo Alice");
        chat2.addMessage(message2);
        dao.add(chat2);

        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(bob);
        contacts.add(charlie);
        Chat chat3 = new Chat(alice, contacts);
        chat3.setTitle("Chat mit Bob und Charlie");
        Message message3 = new Message(charlie);
        message3.setContent("Hallo Alice");
        chat3.addMessage(message3);
        Message message4 = new Message(alice);
        message4.setContent("Hallo ihr zwei");
        chat3.addMessage(message4);
        Assert.assertEquals(2, dao.size());
        dao.add(chat3);
        Assert.assertEquals(3, dao.size());
    }

    @Test
    public void getAllChatsTest(){
        ChatDao dao = new ChatDao(new InMemoSharkKB(), contactDao);
        Chat chat1 = new Chat(alice, bob);
        chat1.setTitle("Chat mit Bob");
        Message message1 = new Message(alice);
        message1.setContent("Hallo Bob");
        chat1.addMessage(message1);
        dao.add(chat1);

        Chat chat2 = new Chat(alice, charlie);
        chat2.setTitle("Chat mit Charlie");
        Message message2 = new Message(charlie);
        message2.setContent("Hallo Alice");
        chat2.addMessage(message2);
        dao.add(chat2);

        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(bob);
        contacts.add(charlie);
        Chat chat3 = new Chat(alice, contacts);
        chat3.setTitle("Chat mit Bob und Charlie");
        Message message3 = new Message(charlie);
        message3.setContent("Hallo Alice");
        chat3.addMessage(message3);
        Message message4 = new Message(alice);
        message4.setContent("Hallo ihr zwei");
        chat3.addMessage(message4);
        dao.add(chat3);

        List<Chat> all = dao.getAll();
        Assert.assertTrue(all.contains(chat1));
        Assert.assertTrue(all.contains(chat2));
        Assert.assertTrue(all.contains(chat3));
    }

    @Test
    public void removeChatTest() throws InterruptedException {
        ChatDao dao = new ChatDao(new InMemoSharkKB(), contactDao);
        Chat chat1 = new Chat(alice, bob);
        chat1.setTitle("Chat mit Bob");
        Message message1 = new Message(alice);
        message1.setContent("Hallo Bob");
        chat1.addMessage(message1);
        dao.add(chat1);
        Thread.sleep(2);

        Chat chat2 = new Chat(alice, charlie);
        chat2.setTitle("Chat mit Charlie");
        Message message2 = new Message(charlie);
        message2.setContent("Hallo Alice");
        chat2.addMessage(message2);
        dao.add(chat2);
        Thread.sleep(2);

        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(bob);
        contacts.add(charlie);
        Chat chat3 = new Chat(alice, contacts);
        chat3.setTitle("Chat mit Bob und Charlie");
        Message message3 = new Message(charlie);
        message3.setContent("Hallo Alice");
        chat3.addMessage(message3);
        Message message4 = new Message(alice);
        message4.setContent("Hallo ihr zwei");
        chat3.addMessage(message4);
        dao.add(chat3);

        Assert.assertEquals(3, dao.size());
        dao.remove(chat2);
        Assert.assertEquals(2, dao.size());
        Assert.assertNull(dao.get(chat2.getId()));
    }

    @Test
    public void updateChatTest(){
        ChatDao dao = new ChatDao(new InMemoSharkKB(), contactDao);
        Chat chat1 = new Chat(alice, bob);
        chat1.setTitle("Chat mit Bob");
        Message message1 = new Message(alice);
        message1.setContent("Hallo Bob");
        chat1.addMessage(message1);
        dao.add(chat1);

        Chat chat2 = new Chat(alice, charlie);
        chat2.setTitle("Chat mit Charlie");
        Message message2 = new Message(charlie);
        message2.setContent("Hallo Alice");
        chat2.addMessage(message2);
        dao.add(chat2);

        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(bob);
        contacts.add(charlie);
        Chat chat3 = new Chat(alice, contacts);
        chat3.setTitle("Chat mit Bob und Charlie");
        Message message3 = new Message(charlie);
        message3.setContent("Hallo Alice");
        chat3.addMessage(message3);
        Message message4 = new Message(alice);
        message4.setContent("Hallo ihr zwei");
        chat3.addMessage(message4);
        dao.add(chat3);

        Message message5 = new Message(alice);
        message5.setContent("Hallo Charlie");
        chat2.addMessage(message5);
        chat2.setTitle("Alle zusammen");
        chat2.addContact(bob);
        dao.update(chat2);

        Assert.assertEquals(3, dao.size());
        Chat savedChat = dao.get(chat2.getId());
        Assert.assertTrue(chat2.equals(savedChat));
    }

    @Test
    public void addChatWithoutTitleAndMessagesTest(){
        ChatDao dao = new ChatDao(new InMemoSharkKB(), contactDao);
        Chat chat = new Chat(alice, bob);
        dao.add(chat);
        List<Chat> all = dao.getAll();
        Assert.assertEquals(1, all.size());
        Chat savedChat = all.get(0);

        Assert.assertTrue(chat.equals(savedChat));
    }

    @Test
    public void addChatWithoutTitleTest(){
        ChatDao dao = new ChatDao(new InMemoSharkKB(), contactDao);
        Chat chat = new Chat(alice, bob);
        Message message = new Message(bob);
        message.setContent("Message");
        chat.addMessage(message);
//        chat.setTitle("Title");
        dao.add(chat);
        Chat savedChat = dao.get(chat.getId());
        Assert.assertTrue(chat.equals(savedChat));
    }

    @Test
    public void addChatWithoutMessageTest(){
        ChatDao dao = new ChatDao(new InMemoSharkKB(), contactDao);
        Chat chat = new Chat(alice, bob);
//        Message message = new Message(bob);
//        message.setContent("Message");
//        chat.addMessage(message);
        chat.setTitle("Title");
        dao.add(chat);
        List<Chat> all = dao.getAll();
        Assert.assertEquals(1, all.size());
        Chat savedChat = all.get(0);

        Assert.assertTrue(chat.equals(savedChat));
    }
}