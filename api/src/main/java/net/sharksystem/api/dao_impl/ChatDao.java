package net.sharksystem.api.dao_impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.serialization.ASIPMessageSerializerHelper;
import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.persistent.sql.SqlSharkKB;
import net.sharkfw.knowledgeBase.sync.SyncKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.knowledgeBase.sync.manager.SyncManager;
import net.sharkfw.knowledgeBase.sync.manager.port.SyncAcceptKP;
import net.sharkfw.knowledgeBase.sync.manager.port.SyncInviteKP;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_interfaces.ContactDao;
import net.sharksystem.api.dao_interfaces.DataAccessObject;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;
import net.sharksystem.api.utils.SharkNetUtils;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 3/22/17.
 */

public class ChatDao implements DataAccessObject<Chat, SemanticTag>, SyncAcceptKP.SyncAcceptListener, SyncInviteKP.SyncInviteListener {

    private final static SemanticTag CONFIG_TYPE = InMemoSharkKB.createInMemoSemanticTag("CONFIG", "si:config");
    private final static SemanticTag INFO_TYPE = InMemoSharkKB.createInMemoSemanticTag("INFO", "si:INFO");
    private final static String CHAT_IMAGE = "CHAT_IMAGE";
    private final static String CHAT_TITLE = "CHAT_TITLE";
    private final static String CHAT_OWNER = "CHAT_OWNER";

    private final static String SYNC_OWNER = "SYNC_OWNER";
    private final static String SYNC_MEMBERS = "SYNC_MEMBERS";
    private final static String SYNC_APPROVED_MEMBERS = "SYNC_APPROVED_MEMBERS";
    private final static String SYNC_WRITABLE = "SYNC_WRITABLE";
    private final static String SYNC_COMPONENT_NAME = "SYNC_COMPONENT_NAME";
    private final static String SYNC_KB_NAME = "SYNC_KB_NAME";

    private final ContactDao mContactDao;
    private final SharkNetApi mApi;
    private final Context mContext;
    private SharkEngine mEngine;

    private SharkKB mRootKb;
    private SqlSharkKB mInfoKb;

    public ChatDao(Context context, SharkNetApi api, SharkEngine engine, SharkKB rootKb, ContactDao contactDao) {
        mApi = api;
        mEngine = engine;
        mRootKb = rootKb;
        mContactDao = contactDao;
        mContext = context;

        if(context!=null){
            initChatInfoKb();

            try {
                Iterator<ASIPInformationSpace> allInformationSpaces = mInfoKb.getAllInformationSpaces();
                if(allInformationSpaces.hasNext()){
                    populateKbsToChats();
                }
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
            mEngine.getSyncManager().addSyncAcceptListener(this);
            mEngine.getSyncManager().addSyncInviteListener(this);
        }

    }

    private void initChatInfoKb(){
        File chatInfoKb = new File(mContext.getExternalFilesDir(null), "infoChat01.db");
        L.d(chatInfoKb.getAbsolutePath(), this);
        mInfoKb = new SqlSharkKB("jdbc:sqldroid:" + chatInfoKb.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", streamToSqlMeta());
    }

    private void addOrUpdateChatInInfoKb(SyncComponent component, SemanticTag tag) throws SharkKBException, JSONException {
        ASIPSpace space = mInfoKb.createASIPSpace(INFO_TYPE, tag, null, null, null, null, null,  ASIPSpace.DIRECTION_INOUT);

        SharkNetUtils.setInfoWithName(mInfoKb, space, SYNC_OWNER, ASIPMessageSerializerHelper.serializeTag(component.getOwner()).toString());
        SharkNetUtils.setInfoWithName(mInfoKb, space, SYNC_MEMBERS, ASIPMessageSerializerHelper.serializeSTSet(component.getMembers()).toString());
        SharkNetUtils.setInfoWithName(mInfoKb, space, SYNC_APPROVED_MEMBERS, ASIPMessageSerializerHelper.serializeSTSet(component.getApprovedMembers()).toString());
        SharkNetUtils.setInfoWithName(mInfoKb, space, SYNC_WRITABLE, component.isWritable());
        SharkNetUtils.setInfoWithName(mInfoKb, space, SYNC_COMPONENT_NAME, ASIPMessageSerializerHelper.serializeTag(component.getUniqueName()).toString());
        SharkNetUtils.setInfoWithName(mInfoKb, space, SYNC_KB_NAME, tag.getSI()[0]);
    }

    private void removeChat(Chat chat) throws SharkKBException {
        ASIPSpace space = mInfoKb.createASIPSpace(INFO_TYPE, chat.getId(), null, null, null, null, null,  ASIPSpace.DIRECTION_INOUT);
        mInfoKb.removeInformationSpace(space);

        File chatFolder = new File(mContext.getExternalFilesDir(null), "chats");
        chatFolder.mkdirs();
        File chatDb = new File(chatFolder, chat.getId().getSI()[0] + ".db");
        chatDb.delete();
    }

    private void populateKbsToChats() throws SharkKBException {
        File chatFolder = new File(mContext.getExternalFilesDir(null), "chats");
        chatFolder.mkdirs();
        try {
            Iterator<ASIPInformationSpace> informationSpaces = mInfoKb.getAllInformationSpaces();
            while (informationSpaces.hasNext()){
                ASIPInformationSpace next = informationSpaces.next();
                ASIPSpace space = next.getASIPSpace();

                String syncOwnerString = SharkNetUtils.getInfoAsString(mInfoKb, space, SYNC_OWNER);
                PeerSemanticTag syncOwner = null;
                if(syncOwnerString!=null && !syncOwnerString.isEmpty()){
                    syncOwner = ASIPMessageSerializerHelper.deserializePeerTag(syncOwnerString);
                }
                String syncMembersString = SharkNetUtils.getInfoAsString(mInfoKb, space, SYNC_MEMBERS);
                PeerSTSet syncMembers = InMemoSharkKB.createInMemoPeerSTSet();
                if(syncMembersString!=null && !syncMembersString.isEmpty()){
                    syncMembers = ASIPMessageSerializerHelper.deserializePeerSTSet(syncMembers, syncMembersString);
                }
                String syncApprovedMembersString = SharkNetUtils.getInfoAsString(mInfoKb, space, SYNC_APPROVED_MEMBERS);
                PeerSTSet syncApprovedMembers = InMemoSharkKB.createInMemoPeerSTSet();
                if(syncApprovedMembersString!=null && !syncApprovedMembersString.isEmpty()){
                    syncApprovedMembers = ASIPMessageSerializerHelper.deserializePeerSTSet(syncMembers, syncApprovedMembersString);
                }
                boolean synIsWritable = SharkNetUtils.getInfoAsBoolean(mInfoKb, space, SYNC_WRITABLE);
                String syncComponentNameString = SharkNetUtils.getInfoAsString(mInfoKb, space, SYNC_COMPONENT_NAME);
                SemanticTag syncComponentName = null;
                if(syncComponentNameString!=null && !syncComponentNameString.isEmpty()){
                    syncComponentName = ASIPMessageSerializerHelper.deserializeTag(syncComponentNameString);
                }
                String syncKbName = SharkNetUtils.getInfoAsString(mInfoKb, space, SYNC_KB_NAME);

                File chat = new File(chatFolder, syncKbName + ".db");
                L.d(chat.getAbsolutePath(), this);
                SqlSharkKB chatSqlKb = new SqlSharkKB("jdbc:sqldroid:" + chat.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", streamToSqlMeta());

                SyncComponent syncComponent = mEngine.getSyncManager().createInvitedSyncComponent(chatSqlKb, syncComponentName, syncMembers, syncApprovedMembers, syncOwner, synIsWritable);
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    private InputStream streamToSqlMeta(){
        try {
            return mContext.getResources().getAssets().open("sharknet.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(Chat object) {
        try {
            File chatFolder = new File(mContext.getExternalFilesDir(null), "chats");
            chatFolder.mkdirs();

            File chat = new File(chatFolder, object.getId().getSI()[0] + ".db");
            L.d(chat.getAbsolutePath(), this);
            SqlSharkKB sharkKB = new SqlSharkKB("jdbc:sqldroid:" + chat.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", streamToSqlMeta());

            // als nächstes holen wir uns alle contacts und wandeln sie zu einem pst
            PeerSTSet contactSet = InMemoSharkKB.createInMemoPeerSTSet();
            ContactDaoImpl contactDao = new ContactDaoImpl(sharkKB);
            for (Contact contact : object.getContacts()) {
                contactSet.merge(contact.getTagAsInMemoTag());
                contactDao.add(contact);
            }
            // Nun müssen wir alle Daten in die kb schreiben! Womöglich bevor die SyncComponent erzeugt wird
            STSet inMemoSTSet = InMemoSharkKB.createInMemoSTSet();
            inMemoSTSet.merge(CONFIG_TYPE);
            STSet topicSet = InMemoSharkKB.createInMemoSTSet();
            topicSet.merge(object.getId());
            ASIPSpace asipSpace = sharkKB.createASIPSpace(topicSet, inMemoSTSet, null, object.getOwner().getTagAsInMemoTag(), contactSet, null, null, ASIPSpace.DIRECTION_INOUT);

            if (object.getTitle() != null) {
                SharkNetUtils.setInfoWithName(sharkKB, asipSpace, CHAT_TITLE, object.getTitle());
            }

            SharkNetUtils.setInfoWithName(sharkKB, asipSpace, CHAT_OWNER, "Owner");

            Bitmap image = object.getImage();
            if (image != null) {
                // setImage
                // Create an inputStream out of the image
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);
                byte[] byteArray = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                L.d("Chat image size: " + bs.available() + "Bytes.", this);
                SharkNetUtils.setInfoWithName(sharkKB, asipSpace, CHAT_IMAGE, bs);
            }

            MessageDao messageDao = new MessageDao(sharkKB, mContactDao);
            for (Message message : object.getMessages()) {
                messageDao.add(message);
            }

            PeerSemanticTag owner = object.getOwner().getTagAsInMemoTag();
            contactDao.add(object.getOwner());

            // Anzahl contacts + title + date
            SyncComponent syncComponent = mEngine.getSyncManager().createSyncComponent(sharkKB, object.getId(), contactSet, owner, true);
            addOrUpdateChatInInfoKb(syncComponent, object.getId());
        } catch (SharkKBException | IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Chat> getAll() {
        List<Chat> chats = new ArrayList<>();
        for (SyncComponent component : mEngine.getSyncManager().getSyncComponents()) {
            SyncKB kb = component.getKb();
            try {
                Iterator<ASIPInformationSpace> informationSpaces = kb.getInformationSpaces(generateInterest(null));
                while (informationSpaces.hasNext()) {
                    ASIPInformationSpace next = informationSpaces.next();
                    ASIPSpace asipSpace = next.getASIPSpace();

                    if (!asipSpace.getTopics().stTags().hasNext()) continue;
                    SemanticTag chatId = asipSpace.getTopics().stTags().next();

                    PeerSTSet receivers = asipSpace.getReceivers();

                    List<Contact> contacts = new ArrayList<>();
                    Contact owner = null;

                    List<Contact> allContacts = mContactDao.getAll();
                    for (Contact contact : allContacts) {
                        if (SharkCSAlgebra.identical(contact.getTag(), component.getOwner())) {
                            owner = contact;
                        } else if (SharkCSAlgebra.isIn(receivers, contact.getTag())) {
                            contacts.add(contact);
                        }
                    }
                    Chat chat = new Chat(owner, contacts, chatId);
                    chat.setTitle(SharkNetUtils.getInfoAsString(kb, asipSpace, CHAT_TITLE));
                    ASIPInformation information = SharkNetUtils.getInfoByName(kb, asipSpace, CHAT_IMAGE);
                    if (information != null) {
                        chat.setImage(BitmapFactory.decodeStream(new ByteArrayInputStream(information.getContentAsByte())));
                    }
                    MessageDao messageDao = new MessageDao(kb, mContactDao);
                    chat.setMessages(messageDao.getAll());

                    chats.add(chat);
                }
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(chats);
        return chats;
    }

    @Override
    public Chat get(SemanticTag id) {
        SyncComponent component = mEngine.getSyncManager().getComponentByName(id);
        if(component!=null){
            SyncKB kb = component.getKb();
            try {
                Chat chat = null;
                Iterator<ASIPInformationSpace> informationSpaces = kb.getInformationSpaces(generateInterest(null));
                while (informationSpaces.hasNext()) {
                    ASIPInformationSpace next = informationSpaces.next();
                    ASIPSpace asipSpace = next.getASIPSpace();

                    SemanticTag chatId = asipSpace.getTopics().stTags().next();

                    PeerSemanticTag senderTag = asipSpace.getSender();
                    PeerSTSet receivers = asipSpace.getReceivers();

                    List<Contact> contacts = new ArrayList<>();
                    Contact owner = null;

                    List<Contact> allContacts = mContactDao.getAll();
                    for (Contact contact : allContacts) {
                        if (SharkCSAlgebra.identical(contact.getTag(), senderTag)) {
                            owner = contact;
                        } else if (SharkCSAlgebra.isIn(receivers, contact.getTag())) {
                            contacts.add(contact);
                        }
                    }
                    chat = new Chat(owner, contacts, chatId);
                    chat.setTitle(SharkNetUtils.getInfoAsString(kb, asipSpace, CHAT_TITLE));
                    ASIPInformation information = SharkNetUtils.getInfoByName(kb, asipSpace, CHAT_IMAGE);
                    if (information != null) {
                        chat.setImage(BitmapFactory.decodeStream(new ByteArrayInputStream(information.getContentAsByte())));
                    }
                    MessageDao messageDao = new MessageDao(kb, mContactDao);
                    chat.setMessages(messageDao.getAll());
                }

                return chat;
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void update(Chat object) {

        SyncComponent component = mEngine.getSyncManager().getComponentByName(object.getId());

        if (component != null) {
            SyncKB kb = component.getKb();
            MessageDao messageDao = new MessageDao(kb, mContactDao);
            messageDao.update(object.getMessages());

            ContactDaoImpl contactDao = new ContactDaoImpl(kb);

            try {
                kb.removeInformationSpace(generateInterest(null));
                // als nächstes holen wir uns alle contacts und wandeln sie zu einem pst
                PeerSTSet contactSet = InMemoSharkKB.createInMemoPeerSTSet();
                for (Contact contact : object.getContacts()) {
                    contactSet.merge(contact.getTagAsInMemoTag());

                    L.d("Exchanging contacts", this);

                    if (contact.equals(mApi.getAccount())){
                        L.d("Set my Account as Contact", this);
                        contact = mApi.getAccount();
                    }

//                    if(object.getImage()==null){
//                        Contact contactOwn = mContactDao.get(contact.getTag());
//                        if(contactOwn.getImage()!=null){
//                            contact.setImage(contactOwn.getImage());
//                        }
//                    }
                    contactDao.update(contact);
                }

                component.getMembers().merge(contactSet);

                // Nun müssen wir alle Daten in die kb schreiben! Womöglich bevor die SyncComponent erzeugt wird
                STSet inMemoSTSet = InMemoSharkKB.createInMemoSTSet();
                inMemoSTSet.merge(CONFIG_TYPE);
                STSet topicSet = InMemoSharkKB.createInMemoSTSet();
                topicSet.merge(object.getId());
                ASIPSpace asipSpace = kb.createASIPSpace(topicSet, inMemoSTSet, null, object.getOwner().getTagAsInMemoTag(), contactSet, null, null, ASIPSpace.DIRECTION_INOUT);

                if (object.getTitle() != null) {
                    SharkNetUtils.setInfoWithName(kb, asipSpace, CHAT_TITLE, object.getTitle());
                }

                if (object.getImage() != null) {
                    // setImage
                    // Create an inputStream out of the image
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    object.getImage().compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);
                    byte[] byteArray = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                    L.d("Chat image size: " + bs.available() + "Bytes.", this);
                    SharkNetUtils.setInfoWithName(kb, asipSpace, CHAT_IMAGE, bs);
                }

                addOrUpdateChatInInfoKb(component, object.getId());

                // TODO Causes Concurrent Thread Problem because of multiple BTConnections.
                mEngine.getSyncManager().doInviteOrSync(component);
            } catch (SharkKBException | IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            add(object);
        }

    }

    @Override
    public void remove(Chat object) {
        SyncComponent component = mEngine.getSyncManager().getComponentByName(object.getId());
        if (component!=null) mEngine.getSyncManager().removeSyncComponent(component);
        try {
            removeChat(object);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {
        return mEngine.getSyncManager().getSyncComponents().size();
    }

    private SharkKB createKBFromRoot() throws SharkKBException {
        return new InMemoSharkKB(InMemoSharkKB.createInMemoSemanticNet(), InMemoSharkKB.createInMemoSemanticNet(), mRootKb.getPeersAsTaxonomy(), InMemoSharkKB.createInMemoSpatialSTSet(), InMemoSharkKB.createInMemoTimeSTSet());
    }

    private ASIPSpace generateInterest(SemanticTag id) {
        try {
            STSet inMemoSTSet = InMemoSharkKB.createInMemoSTSet();
            inMemoSTSet.merge(CONFIG_TYPE);
            STSet topicSet = null;
            if (id != null) {
                topicSet = InMemoSharkKB.createInMemoSTSet();
                topicSet.merge(id);
            }
            return InMemoSharkKB.createInMemoASIPInterest(topicSet, inMemoSTSet, null, null, null, null, null, ASIPSpace.DIRECTION_INOUT);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onSyncInviteAccepted(SyncComponent component, PeerSTSet approvers) {

    }

    @Override
    public void onSyncInvitation(ASIPInterest interest) {
        File chatFolder = new File(mContext.getExternalFilesDir(null), "chats");
        chatFolder.mkdirs();
        try {
            Iterator<SemanticTag> topics = interest.getTopics().stTags();
            // Create an empty kb based on the first topic
            SemanticTag next = topics.next();

            interest.getApprovers().merge(mApi.getAccount().getTagAsInMemoTag());

            File chat = new File(chatFolder, next.getSI()[0] + ".db");
            L.d(chat.getAbsolutePath(), this);
            SqlSharkKB chatSqlKb = new SqlSharkKB("jdbc:sqldroid:" + chat.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", streamToSqlMeta());

            SyncComponent component = mEngine.getSyncManager().createInvitedSyncComponent(chatSqlKb, next, interest.getReceivers(), interest.getApprovers(), interest.getSender(), true);
            addOrUpdateChatInInfoKb(component, next);
        } catch (SharkKBException | JSONException e) {
            e.printStackTrace();
        }
    }
}
