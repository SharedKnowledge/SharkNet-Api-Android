package net.sharksystem.sharknet_api_android.impl;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.sharknet_api_android.interfaces.SharkNet;

import org.junit.Before;

/**
 * Created by j4rvis on 11.08.16.
 */
public class BaseTest {

    // Member initiation

    protected SharkNet mSharkNet;

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

    @Before
    public void init() throws SharkKBException {
        mSharkNet = SharkNetEngine.getSharkNet();
    }


}
