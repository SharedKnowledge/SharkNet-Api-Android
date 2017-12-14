package net.sharksystem.api.dao_impl;

import android.content.Context;
import android.graphics.BitmapFactory;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoPeerSTSet;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.persistent.sql.SqlSharkKB;
import net.sharkfw.knowledgeBase.sync.SyncKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.routing.SemanticRoutingKP;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_interfaces.DataAccessObject;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Broadcast;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;
import net.sharksystem.api.utils.SharkNetUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
                this.engine.getBroadcastManager().addSemanticRoutingListener(this);

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
        SyncComponent syncComponent = engine.getBroadcastManager().createSyncComponent(sharkKB, this.broadcast.getId(), new InMemoPeerSTSet(), engine.getOwner(), true);
        SyncKB syncKB = syncComponent.getKb();
        MessageDao messageDao = new MessageDao(syncKB);
        for (Message message : broadcast.getMessages()) {
            messageDao.add(message);
        }
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
        ; // Singleton Pattern, Broadcast is generated in the constructor
    }

    @Override
    public List<Broadcast> getAll() {
        List<Broadcast> broadcasts = new ArrayList<>();
        broadcasts.add(get(broadcast.getId()));
        return broadcasts;
    }

    @Override
    public Broadcast get(SemanticTag id) {
        SyncComponent component = engine.getBroadcastManager().getBroadcastComponent();
        SyncKB kb = component.getKb();
        //Iterator<ASIPInformationSpace> informationSpaces = kb.getInformationSpaces(generateInterest());
        //ASIPInformationSpace next = informationSpaces.next();
        MessageDao messageDao = new MessageDao(kb);
        broadcast.setMessages(messageDao.getAll());
        return broadcast;

    }

    @Override
    public void update(Broadcast object) {
        SyncComponent component = engine.getBroadcastManager().getBroadcastComponent();
        SyncKB kb = component.getKb();
        MessageDao messageDao = new MessageDao(kb);
        messageDao.update(object.getMessages());
    }

    public void update(Broadcast object, Message message, List<PeerSemanticTag> peers) {
        SyncComponent component = engine.getBroadcastManager().getBroadcastComponent();
        SyncKB kb = component.getKb();
        MessageDao messageDao = new MessageDao(kb);
        messageDao.update(object.getMessages());
        SyncComponent compMessage = createComponentMessage(message);
        if (compMessage != null) {
            for (PeerSemanticTag peer: peers) {
                engine.getBroadcastManager().sendBroadcastMessage(compMessage, peer);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //engine.getBroadcastManager().sendBroadcastMessage(compMessage, peers);
        }
        else {
            L.w("Could not create SyncComponent!");
        }
    }

    @Override
    public void remove(Broadcast object) {
        ; // Singleton Pattern, Broadcast is generated in the constructor
    }

    @Override
    public int size() {
        return broadcast.getMessages().size();
    }

    @Override
    public void onNewMerge(SyncComponent component, SharkKB changes, boolean accepted, boolean forwarded) {

    }

    private SyncComponent createComponentMessage(Message object) {
        SyncComponent component = null;
        SharkKB kb = new InMemoSharkKB();
        SemanticTag topic = InMemoSharkKB.createInMemoSemanticTag(Message.MESSAGE_ID, object.getSender().getTag().getName() + object.getDate().getTime());
        ASIPSpace asipSpace;
        try {
            if (object.getTopic() != null) {
                asipSpace = kb.createASIPSpace(object.getTopic(), MessageDao.MESSAGE_TYPE, null, object.getSender().getTag(), null, object.getTime(), object.getLocation(), ASIPSpace.DIRECTION_INOUT);
            } else {
                asipSpace = kb.createASIPSpace(topic, MessageDao.MESSAGE_TYPE, null, object.getSender().getTag(), null, object.getTime(), object.getLocation(), ASIPSpace.DIRECTION_INOUT);
            }

            SharkNetUtils.setInfoWithName(kb, asipSpace, MessageDao.MESSAGE_CONTENT, object.getContent());
            SharkNetUtils.setInfoWithName(kb, asipSpace, MessageDao.MESSAGE_DATE, object.getDate().getTime());
            SharkNetUtils.setInfoWithName(kb, asipSpace, MessageDao.MESSAGE_VERIFIED, object.isVerified());
            SharkNetUtils.setInfoWithName(kb, asipSpace, MessageDao.MESSAGE_SIGNED, object.isSigned());
            SharkNetUtils.setInfoWithName(kb, asipSpace, MessageDao.MESSAGE_ENCRYPTED, object.isEncrypted());
        }
        catch (SharkKBException e) {
            e.printStackTrace();
            return null;
        }
        try {
            component = new SyncComponent(kb, this.broadcast.getId(), new InMemoPeerSTSet(), engine.getOwner(), true);
        } catch (SharkKBException e) {
            e.printStackTrace();
            return null;
        }

        return component;
    }
}
