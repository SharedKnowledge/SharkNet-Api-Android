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

}
