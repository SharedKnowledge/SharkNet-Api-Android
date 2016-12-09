package net.sharksystem.api.shark.ports;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.ASIPConnection;
import net.sharkfw.asip.engine.ASIPInMessage;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.peer.KnowledgePort;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeerManager;

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
    }

    @Override
    protected void handleExpose(ASIPInMessage message, ASIPConnection asipConnection, ASIPInterest interest) throws SharkKBException {

        if(interest == null) return;

        STSet topics = interest.getTopics();

        if(topics == null || topics.isEmpty()) return;

        SemanticTag topicsSemanticTag = topics.getSemanticTag(AndroidSharkEngine.DISCOVERY_SI);

        if(topicsSemanticTag == null) return;

        mNearbyPeerManager.addPeer(interest);
    }
}
