package net.sharksystem.api.shark.peer;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.engine.ASIPOutMessage;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoKnowledge;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.system.SharkTask;

/**
 * Created by j4rvis on 11/23/16.
 */

public class SyncTask extends SharkTask {

    private final String mContent;
    private final NearbyPeer mPeer;
    private final SharkEngine mEngine;

    public SyncTask(SharkEngine engine, String content, NearbyPeer peer) {
        mEngine = engine;
        mContent = content;
        mPeer = peer;
    }

    @Override
    protected Object process() {
        // TODO Create a Message and send it!

        ASIPOutMessage asipOutMessage = mEngine.createASIPOutMessage(mPeer.getSender().getAddresses(), mPeer.getSender());
        InMemoKnowledge inMemoKnowledge = new InMemoKnowledge();
        try {
            STSet topics = InMemoSharkKB.createInMemoSTSet();
            topics.createSemanticTag("TEST", "www.test.de");
            ASIPInterest interest = InMemoSharkKB.createInMemoASIPInterest(topics, null, (PeerSemanticTag) null, null, null, null, null, ASIPInterest.DIRECTION_INOUT);
            inMemoKnowledge.addInformation(mContent, interest);
            asipOutMessage.insert(inMemoKnowledge);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
