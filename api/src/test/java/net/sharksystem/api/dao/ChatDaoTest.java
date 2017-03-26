package net.sharksystem.api.dao;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.ChatDao;
import net.sharksystem.api.dao_impl.ContactDao;
import net.sharksystem.api.dao_impl.SharkNetApi;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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

    private ChatDao dao;
    private ContactDao contactDao;

    @Before
    public void setUp() throws Exception {
        L.setLogLevel(L.LOGLEVEL_ALL);
        dao = SharkNetApi.getInstance().getChatDao();
        contactDao = SharkNetApi.getInstance().getContactDao();
    }

    @Test
    public void addChatTest(){
        Contact alice = new Contact(aliceTag);
        Contact bob = new Contact(bobTag);
        Contact charlie = new Contact(charlieTag);
        contactDao.add(alice);
        contactDao.add(bob);
        contactDao.add(charlie);
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
        Contact alice = new Contact(aliceTag);
        Contact bob = new Contact(bobTag);
        Contact charlie = new Contact(charlieTag);
        contactDao.add(alice);
        contactDao.add(bob);
        contactDao.add(charlie);
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
    public void getAllChatsTest(){
        Contact alice = new Contact(aliceTag);
        Contact bob = new Contact(bobTag);
        Contact charlie = new Contact(charlieTag);
        contactDao.add(alice);
        contactDao.add(bob);
        contactDao.add(charlie);
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
        int size = dao.size();
        dao.add(chat3);

        // TODO no resetting of ChatDao. so more chats cause of multiple execution of tests.
        Assert.assertEquals(size+1, dao.size());
    }

    @Test
    public void removeChatTest(){
        Contact alice = new Contact(aliceTag);
        Contact bob = new Contact(bobTag);
        Contact charlie = new Contact(charlieTag);
        contactDao.add(alice);
        contactDao.add(bob);
        contactDao.add(charlie);
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

        int size = dao.size();
        dao.remove(chat2);
        Assert.assertEquals(size-1, dao.size());
        Assert.assertNull(dao.get(chat2.getId()));
    }

    @Test
    public void updateChatTest(){
        Contact alice = new Contact(aliceTag);
        Contact bob = new Contact(bobTag);
        Contact charlie = new Contact(charlieTag);
        contactDao.add(alice);
        contactDao.add(bob);
        contactDao.add(charlie);
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
}