package net.sharksystem.api.shark.ports;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.ASIPConnection;
import net.sharkfw.asip.engine.ASIPInMessage;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.peer.KnowledgePort;
import net.sharkfw.system.L;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeerManager;

import java.util.Iterator;

/**
 * Created by j4rvis on 11/22/16.
 */

public class RadarDiscoveryPort extends KnowledgePort {

    private final NearbyPeerManager mNearbyPeerManager;

    public RadarDiscoveryPort(AndroidSharkEngine se) {
        super(se);
        mNearbyPeerManager = NearbyPeerManager.getInstance();
    }

    @Override
    protected void handleInsert(ASIPInMessage message, ASIPConnection asipConnection, ASIPKnowledge asipKnowledge) {
        // do nothing

        // TODO okay now check if we got the insert!

        try {
            STSet topics = InMemoSharkKB.createInMemoSTSet();
            topics.createSemanticTag("TEST", "www.test.de");
            ASIPInterest interest = InMemoSharkKB.createInMemoASIPInterest(topics, null, (PeerSemanticTag) null, null, null, null, null, ASIPInterest.DIRECTION_INOUT);
            Iterator<ASIPInformation> information = asipKnowledge.getInformation(interest);

            while (information.hasNext()){
                ASIPInformation next = information.next();
                String contentAsString = next.getContentAsString();

                L.d("----INCOMING MESSAGE-----", this);
                L.d(contentAsString, this);
                L.d("-------------------------", this);
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void handleExpose(ASIPInMessage message, ASIPConnection asipConnection, ASIPInterest interest) throws SharkKBException {

        if(interest == null) return;

        STSet topics = interest.getTopics();

        if(topics == null || topics.isEmpty()) return;

        SemanticTag topicsSemanticTag = topics.getSemanticTag(AndroidSharkEngine.DISCOVERY_SI);

        if(topicsSemanticTag == null) return;

        // Now let the PeerManager do it's magic!!!

        mNearbyPeerManager.addPeer(interest);
    }
}
