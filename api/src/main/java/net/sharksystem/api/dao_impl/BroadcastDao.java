package net.sharksystem.api.dao_impl;

import android.content.Context;

import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoPeerSTSet;
import net.sharkfw.knowledgeBase.persistent.sql.SqlSharkKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.routing.SemanticRoutingKP;
import net.sharksystem.api.dao_interfaces.DataAccessObject;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Broadcast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dustin Feurich
 */


public class BroadcastDao implements DataAccessObject<Broadcast, SemanticTag>, SemanticRoutingKP.SemanticRoutingListener {

    private final SharkNetApi api;
    private final Context context;
    private SharkEngine engine;
    private SharkKB kb;
    private Broadcast broadcast;


    public BroadcastDao(Context context, SharkNetApi api, SharkEngine engine, SharkKB kb) {
        this.api = api;
        this.engine = engine;
        this.kb = kb;
        this.context = context;
        this.broadcast = new Broadcast();
        if(context!=null){
            try {
                getKbForBroadcast();
                this.engine.getSyncManager().addSemanticRoutingListener(this);
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }
    }

    private void getKbForBroadcast() throws SharkKBException {
        File chatFolder = new File(context.getExternalFilesDir(null), "chats");
        chatFolder.mkdirs();
        File chatFile = new File(chatFolder,   "broadcast.db");
        SharkKB sharkKB =  new SqlSharkKB("jdbc:sqldroid:" + chatFile.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", streamToSqlMeta());
        PeerSTSet peerSet = new InMemoPeerSTSet();
        peerSet.createPeerSemanticTag(engine.getOwner().getName(),engine.getOwner().getSI(), engine.getOwner().getAddresses());
        SyncComponent syncComponent = engine.getSyncManager().createSyncComponent(sharkKB, this.broadcast.getId(), new InMemoPeerSTSet(), engine.getOwner(), true);
    }

    private InputStream streamToSqlMeta(){
        try {
            return context.getResources().getAssets().open("sharknet.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void add(Broadcast object) {

    }

    @Override
    public List<Broadcast> getAll() {
        return null;
    }

    @Override
    public Broadcast get(SemanticTag id) {
        return null;
    }

    @Override
    public void update(Broadcast object) {

    }

    @Override
    public void remove(Broadcast object) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void onNewMerge(SyncComponent component, SharkKB changes) {

    }
}
