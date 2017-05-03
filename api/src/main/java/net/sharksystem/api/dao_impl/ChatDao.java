package net.sharksystem.api.dao_impl;

import android.graphics.Bitmap;
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
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.sync.SyncKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_interfaces.DataAccessObject;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;
import net.sharksystem.api.utils.SharkNetUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 3/22/17.
 */

public class ChatDao implements DataAccessObject<Chat, SemanticTag> {

    private final static SemanticTag CONFIG_TYPE = InMemoSharkKB.createInMemoSemanticTag("CONFIG", "si:config");
    private final static String CHAT_IMAGE = "CHAT_IMAGE";
    private final static String CHAT_TITLE = "CHAT_TITLE";
    private final static String CHAT_OWNER = "CHAT_OWNER";
    private final ContactDao mContactDao;
    private SharkEngine mEngine;

    private SharkKB mRootKb;
    private List<SyncComponent> mSyncComponentList = new ArrayList<>();

    public ChatDao(SharkEngine engine, SharkKB rootKb, ContactDao contactDao) {
        mEngine = engine;
        mRootKb = rootKb;
        mContactDao = contactDao;
        mSyncComponentList = mEngine.getSyncManager().getSyncComponents();
    }

    public ChatDao(SharkKB rootKb, ContactDao contactDao) {
        mRootKb = rootKb;
        mContactDao = contactDao;
    }

    @Override
    public void add(Chat object) {
        try {
            // als aller erstes müssen wir nun eine neue kb erzeugen auf Basis der rootKB
            SharkKB sharkKB = createKBFromRoot();

            // als nächstes holen wir uns alle contacts und wandeln sie zu einem pst
            PeerSTSet contactSet = InMemoSharkKB.createInMemoPeerSTSet();
            for (Contact contact : object.getContacts()) {
                contactSet.merge(contact.getTag());
            }
            // Nun müssen wir alle Daten in die kb schreiben! Womöglich bevor die SyncComponent erzeugt wird
            STSet inMemoSTSet = InMemoSharkKB.createInMemoSTSet();
            inMemoSTSet.merge(CONFIG_TYPE);
            STSet topicSet = InMemoSharkKB.createInMemoSTSet();
            topicSet.merge(object.getId());
            ASIPSpace asipSpace = sharkKB.createASIPSpace(topicSet, inMemoSTSet, null, object.getOwner().getTag(), contactSet, null, null, ASIPSpace.DIRECTION_INOUT);

            if(object.getTitle()!=null){
                SharkNetUtils.setInfoWithName(sharkKB, asipSpace, CHAT_TITLE, object.getTitle());
            }

            SharkNetUtils.setInfoWithName(sharkKB, asipSpace, CHAT_OWNER, "Owner");

            Bitmap image = object.getImage();
            if (image != null) {
                // setImage
                // Create an inputStream out of the image
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] byteArray = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                SharkNetUtils.setInfoWithName(sharkKB, asipSpace, CHAT_IMAGE, bs);
            }

            MessageDao messageDao = new MessageDao(sharkKB, mContactDao);
            for (Message message : object.getMessages()) {
                messageDao.add(message);
            }

            // wir benötigen noch den owner des chats
            PeerSemanticTag owner = object.getOwner().getTag();
            // Und nun noch eine möglichst einzigartige ID!!
            // Anzahl contacts + title + date
            if(mEngine!=null){
                mEngine.getSyncManager().createSyncComponent(sharkKB, object.getId(), contactSet, owner, true);
            } else {
                SyncComponent syncComponent = new SyncComponent(sharkKB, object.getId(), contactSet, owner, true);
                mSyncComponentList.add(syncComponent);
            }
        } catch (SharkKBException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Chat> getAll() {
//        L.d("Number of components: " + mEngine.getSyncManager().getSyncComponents().size(), this);
        List<Chat> chats = new ArrayList<>();
        for (SyncComponent component : mEngine.getSyncManager().getSyncComponents()) {
            SyncKB kb = component.getKb();
            try {
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

        for (SyncComponent component : mSyncComponentList) {
            if (SharkCSAlgebra.identical(component.getUniqueName(), id)) {

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
        }
        return null;
    }

    @Override
    public void update(Chat object) {
        for (SyncComponent component : mSyncComponentList) {
            if (SharkCSAlgebra.identical(component.getUniqueName(), object.getId())) {
                SyncKB kb = component.getKb();
                MessageDao messageDao = new MessageDao(kb, mContactDao);
                messageDao.update(object.getMessages());

                try {
                    kb.removeInformationSpace(generateInterest(null));

                    // als nächstes holen wir uns alle contacts und wandeln sie zu einem pst
                    PeerSTSet contactSet = InMemoSharkKB.createInMemoPeerSTSet();
                    for (Contact contact : object.getContacts()) {
                        contactSet.merge(contact.getTag());
                    }

                    component.getMembers().merge(contactSet);

                    // Nun müssen wir alle Daten in die kb schreiben! Womöglich bevor die SyncComponent erzeugt wird
                    STSet inMemoSTSet = InMemoSharkKB.createInMemoSTSet();
                    inMemoSTSet.merge(CONFIG_TYPE);
                    STSet topicSet = InMemoSharkKB.createInMemoSTSet();
                    topicSet.merge(object.getId());
                    ASIPSpace asipSpace = kb.createASIPSpace(topicSet, inMemoSTSet, null, object.getOwner().getTag(), contactSet, null, null, ASIPSpace.DIRECTION_INOUT);

                    SharkNetUtils.setInfoWithName(kb, asipSpace, CHAT_TITLE, object.getTitle());

                    Bitmap image = object.getImage();
                    if (image != null) {
                        // setImage
                        // Create an inputStream out of the image
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                        byte[] byteArray = bos.toByteArray();
                        ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                        SharkNetUtils.setInfoWithName(kb, asipSpace, CHAT_IMAGE, bs);
                    }
                } catch (SharkKBException | IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    // TODO care for fileSystemImpl. SharkKb needs to be deleted.
    @Override
    public void remove(Chat object) {
        for (SyncComponent component : mSyncComponentList) {
            if (SharkCSAlgebra.identical(component.getUniqueName(), object.getId())) {
                mSyncComponentList.remove(component);
                if(mEngine!=null){
                    mEngine.getSyncManager().removeSyncComponent(component);
                    mSyncComponentList = mEngine.getSyncManager().getSyncComponents();
                }
                return;
            }
        }
    }

    @Override
    public int size() {
        return mSyncComponentList.size();
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
}
