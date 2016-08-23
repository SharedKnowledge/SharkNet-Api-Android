package net.sharksystem.api.impl;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;

import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by j4rvis on 11.08.16.
 */
public class BaseTest {

    // Member initiation

    protected SharkNetEngine mSharkNet;

    // Creating contacts

    // Alice

    protected String aliceName = "Alice";
    protected String aliceNickName = "Ali";
    protected String aliceSI = "www.facebook.com/alice";
    protected String aliceMail = "mail://alice.com";
    protected String aliceTelephoneHome = "030 123456789";
    protected String aliceTelephoneMobile = "01177 123456789";
    protected String aliceNote = "This is just a simple note";

    protected PeerSemanticTag aliceTag = InMemoSharkKB.createInMemoPeerSemanticTag(aliceName, aliceSI, aliceMail);

    protected String bobName = "Bob";
    protected String bobNickName = "Bob";
    protected String bobSI = "www.facebook.com/bob";
    protected String bobMail = "mail://bob.com";
    protected String bobTelephoneHome = "030 1234567891";
    protected String bobTelephoneMobile = "01177 1234567891";
    protected String bobNote = "This is just a simple note from Bob";

    protected PeerSemanticTag bobTag = InMemoSharkKB.createInMemoPeerSemanticTag(bobName, bobSI, bobMail);

    protected String charlieName = "Charlie";
    protected String charlieNickName = "Charlie";
    protected String charlieSI = "www.facebook.com/charlie";
    protected String charlieMail = "mail://charlie.com";
    protected String charlieTelephoneHome = "030 1234567891";
    protected String charlieTelephoneMobile = "01177 1234567891";
    protected String charlieNote = "This is just a simple note from Charlie";

    protected PeerSemanticTag charlieTag = InMemoSharkKB.createInMemoPeerSemanticTag(charlieName, charlieSI, charlieMail);

    byte[] randomByte = new byte[20];
    InputStream stream;

    @Before
    public void init() throws SharkKBException {
        mSharkNet = (SharkNetEngine) SharkNetEngine.getSharkNet();
        new Random().nextBytes(randomByte);
        stream = new ByteArrayInputStream(randomByte);
    }

    @After
    public void clear() throws SharkKBException {
        mSharkNet.clearData();
    }


}
