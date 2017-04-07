package net.sharksystem.api.shark.ports;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.ASIPConnection;
import net.sharkfw.asip.engine.ASIPInMessage;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.ports.KnowledgePort;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.shark.protocols.wifidirect.WifiDirectAdvertisingManager;

/**
 * Created by j4rvis on 11/22/16.
 */

public class RadarDiscoveryPort extends KnowledgePort {

    private final NearbyPeerManager mNearbyPeerManager;

    public RadarDiscoveryPort(AndroidSharkEngine se, NearbyPeerManager peerManager) {
        super(se);
        mNearbyPeerManager = peerManager;
    }

    @Override
    protected void handleInsert(ASIPInMessage message, ASIPConnection asipConnection, ASIPKnowledge asipKnowledge) {
        // do nothing
    }

    @Override
    protected void handleExpose(ASIPInMessage message, ASIPConnection asipConnection, ASIPInterest interest) throws SharkKBException {

        if (interest == null) return;

        STSet types = interest.getTypes();

        if (types == null || types.isEmpty()) return;

        SemanticTag typeSemanticTag = types.getSemanticTag(WifiDirectAdvertisingManager.TYPE_SI);

        if (SharkCSAlgebra.identical(typeSemanticTag, typeSemanticTag)) {
            mNearbyPeerManager.addPeer(interest);
        }

    }
}
