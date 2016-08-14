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

    private SharkNet mSharkNet;

    // Creating contacts

    // Alice

    private String aliceName = "Alice";
    private String aliceNickName = "Ali";
    private String aliceSI = "www.facebook.com/alice";
    private String aliceMail = "mail://alice.com";
    private String aliceTelephoneHome = "030 123456789";
    private String aliceTelephoneMobile = "01177 123456789";
    private String aliceNote = "This is just a simple note";

    private PeerSemanticTag aliceTag = InMemoSharkKB.createInMemoPeerSemanticTag(aliceName, aliceSI, aliceMail);

    @Before
    public void init() throws SharkKBException {
        mSharkNet = SharkNetEngine.getSharkNet();
    }


}
