package net.sharksystem.api.dao_impl;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.ASIPOutMessage;
import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.persistent.sql.SqlSharkKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.security.PkiStorage;
import net.sharkfw.security.SharkPkiStorage;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharksystem.api.dao_interfaces.ContactDao;
import net.sharksystem.api.dao_interfaces.ProfileDao;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Broadcast;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;
import net.sharksystem.api.models.Profile;
import net.sharksystem.api.models.Settings;
import net.sharksystem.api.service.SharkService;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.shark.ports.NfcPkiPort;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;
import net.sharksystem.api.shark.ports.NfcPkiPortListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by j4rvis on 3/26/17.
 */

public class SharkNetApiImpl implements SharkNetApi {

    private final Context mContext;
    private File contactsDb;
    private File settingsDb;
    private File profilesDb;
    private SharkKB mRootKb = new InMemoSharkKB();
    private AndroidSharkEngine mEngine;
    private ChatDao mChatDao;
    private BroadcastDao mBroadcastDao;
    private ContactDao mContactDao;
    private MessageDao mMessageDao;
    private ProfileDao mProfileDao;
    private SettingsDao mSettingsDao;
    private Contact mAccount;
    private boolean mIsDiscovering = false;
    private Intent mIntent;

    public SharkNetApiImpl(Context context) {

        mContext = context;

        InputStream stream3 = null;
        try {
            stream3 = context.getResources().getAssets().open("sharknet.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File storageDb = new File(mContext.getExternalFilesDir(null), "storage01.db");
        L.d(storageDb.getAbsolutePath(), this);
        SqlSharkKB storageKb = new SqlSharkKB("jdbc:sqldroid:" + storageDb.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", stream3);

        mEngine = new AndroidSharkEngine(context, storageKb);

        mEngine.getSyncManager().addSyncMergeListener(this);
        InputStream stream = null;
        InputStream stream2 = null;
        InputStream stream4 = null;
        try {
            stream = context.getResources().getAssets().open("sharknet.sql");
            stream2 = context.getResources().getAssets().open("sharknet.sql");
            stream4 = context.getResources().getAssets().open("sharknet.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        File target = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "databases");
//        File target = new File(mContext.getExternalFilesDir(null), "databases");
//        target.mkdirs();
        contactsDb = new File(mContext.getExternalFilesDir(null), "contacts02.db");
        L.d(contactsDb.getAbsolutePath(), this);
        settingsDb = new File(mContext.getExternalFilesDir(null), "settings02.db");
        L.d(settingsDb.getAbsolutePath(), this);
        profilesDb = new File(mContext.getExternalFilesDir(null), "profiles.db");
        L.d(profilesDb.getAbsolutePath(), this);
        mSettingsDao = new SettingsDao(new SqlSharkKB("jdbc:sqldroid:" + settingsDb.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", stream2));
        mContactDao = new CachedContactDaoImpl(new SqlSharkKB("jdbc:sqldroid:" + contactsDb.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", stream));
        mProfileDao = new ProfileDaoImpl(new SqlSharkKB("jdbc:sqldroid:" + profilesDb.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", stream4));

        mEngine.getSyncManager().allowInvitation(true, true);
        mChatDao = new ChatDao(mContext, this, mEngine, mRootKb, mContactDao);
        mBroadcastDao = new BroadcastDao(mContext, this, mEngine, mRootKb);

//        try {
//            mContactDao = new CachedContactDaoImpl(new InMemoSharkKB(InMemoSharkKB.createInMemoSemanticNet(), InMemoSharkKB.createInMemoSemanticNet(), mRootKb.getPeersAsTaxonomy(), InMemoSharkKB.createInMemoSpatialSTSet(), InMemoSharkKB.createInMemoTimeSTSet()));
//        } catch (SharkKBException e) {
//            e.printStackTrace();
//        }
        Settings settings = getSettings();
        if (settings.getAccountTag() != null) {
            Contact contact = mContactDao.get(settings.getAccountTag());
            if(contact!=null){
                mContactDao.add(contact);
                mEngine.setEngineOwnerPeer(contact.getTag());
                mAccount = contact;
            }
            if (settings.getMailSmtpServer() != null && !settings.getMailSmtpServer().isEmpty() || settings.getMailUsername() != null && !settings.getMailUsername().isEmpty() || settings.getMailPassword() != null && !settings.getMailPassword().isEmpty() || settings.getMailPopServer() != null && !settings.getMailPopServer().isEmpty() || settings.getMailAddress() != null && !settings.getMailAddress().isEmpty()) {

                // TODO
                mEngine.setBasicMailConfiguration(settings.getMailSmtpServer(), settings.getMailUsername(), settings.getMailPassword(), settings.getMailPopServer(), settings.getMailAddress());
                try {
                    mEngine.startMail();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public AndroidSharkEngine getSharkEngine() {
        return mEngine;
    }

    // Account Methods

    public Contact getAccount() {
        return mAccount;
    }

    public void setAccount(Contact contact) {
        if (mAccount == null) {
            mContactDao.add(contact);
            mEngine.setEngineOwnerPeer(contact.getTag());
        } else {
            mContactDao.update(contact);
        }

        mAccount = contact;
        // Update Account in Settings
        Settings settings = getSettings();
        settings.setAccountTag(contact.getTag());
        setSettings(settings);

        if (settings.getMailSmtpServer() != null && !settings.getMailSmtpServer().isEmpty() || settings.getMailUsername() != null && !settings.getMailUsername().isEmpty() || settings.getMailPassword() != null && !settings.getMailPassword().isEmpty() || settings.getMailPopServer() != null && !settings.getMailPopServer().isEmpty() || settings.getMailAddress() != null && !settings.getMailAddress().isEmpty()) {

            // TODO
            mEngine.setBasicMailConfiguration(settings.getMailSmtpServer(), settings.getMailUsername(), settings.getMailPassword(), settings.getMailPopServer(), settings.getMailAddress());
            try {
                mEngine.startMail();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setNotificationResultActivity(Intent intent) {
        mIntent = intent;
    }

    @Override
    public void clearDbs() {
        for (Contact contact : mContactDao.getAll()) {
            mContactDao.remove(contact);
        }
        mSettingsDao.clearSettings();

        // TODO remove chat folder and clear SyncInfoKb.


//        InputStream stream = null;
//        InputStream stream2 = null;
//        try {
//            stream = mContext.getResources().getAssets().open("sharknet.sql");
//            stream2 = mContext.getResources().getAssets().open("sharknet.sql");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        contactsDb = new File(mContext.getExternalFilesDir(null), "contacts02.db");
//        L.d(contactsDb.getAbsolutePath(), this);
//        settingsDb = new File(mContext.getExternalFilesDir(null), "settings02.db");
//        L.d(settingsDb.getAbsolutePath(), this);
//        mSettingsDao = new SettingsDao(new SqlSharkKB("jdbc:sqldroid:" + settingsDb.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", stream2));
//        mContactDao = new CachedContactDaoImpl(new SqlSharkKB("jdbc:sqldroid:" + contactsDb.getAbsolutePath(), "org.sqldroid.SQLDroidDriver", stream));

        mAccount=null;
    }

    @Override
    public void onNewMerge(SyncComponent component, SharkKB changes) {
        changesIncludeContacts(changes);

        MessageDao messageDao = new MessageDao(changes, mContactDao);
        List<Message> all = messageDao.getAll();
        int numberOfMessages = 0;
        if (all != null || !all.isEmpty()) {
            L.d("We have " + all.size() + " new Messages", this);
            numberOfMessages = all.size();
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext).setSmallIcon(android.R.drawable.ic_menu_compass);

        Chat chat = getChat(component.getUniqueName());
        if (chat != null) {
            String chatTitle = "";
            if (chat.getTitle() == null) {
                List<Contact> contacts = chat.getContacts();
                contacts.remove(mAccount);
                if (contacts.size() == 1) chatTitle = "mit " + contacts.get(0).getName();
            } else {
                chatTitle = chat.getTitle();
            }
            mBuilder.setContentTitle("Neue Nachrichten");
            mBuilder.setContentText("Der Chat " + chatTitle + " hat neue Nachrichten");
        } else {
            mBuilder.setContentTitle("Neuer Chat").setContentText(" Du hast einen neuen Chat.");
        }

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotifyMgr = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and   issues it.
        mNotifyMgr.notify(001, mBuilder.build());
    }

    @Override
    public void setSettings(Settings settings) {
        mSettingsDao.setSettings(settings);
    }

    private void changesIncludeContacts(SharkKB changes) {
        ContactDaoImpl contactDao = new ContactDaoImpl(changes);
        List<Contact> all = contactDao.getAll();
        if (all == null || all.isEmpty()) return;
        for (Contact contact : all) {
            if (!contact.equals(mAccount)) {
                L.d("Begin to update contact", this);
                updateContact(contact);
                L.d("New contact: " + contact.getName(), this);
                if (contact.getImage() != null) {
                    L.d("Image size: " + contact.getImage().getByteCount(), this);
                }
            }
        }
    }

    @Override
    public Settings getSettings() {
        return mSettingsDao.getSettings();
    }

    // DAO Methods

    public List<Chat> getChats() {
        return mChatDao.getAll();
    }

    public Chat getChat(SemanticTag tag) {
        return mChatDao.get(tag);
    }

    public void addChat(Chat chat) {
        mChatDao.add(chat);
    }

    public void updateChat(Chat chat) {
        mChatDao.update(chat);
    }

    public void removeChat(Chat chat) {
        mChatDao.remove(chat);
    }

    public int numberOfChats() {
        return mChatDao.size();
    }

    public Broadcast getBroadcast() {return mBroadcastDao.get(null) /*return new Broadcast()*/;}

    public void updateBroadcast(Broadcast broadcast) {mBroadcastDao.update(broadcast);}

    public void updateBroadcast(Broadcast broadcast, Message message, List<PeerSemanticTag> peers) {mBroadcastDao.update(broadcast, message, peers);}

    public List<Contact> getContacts() {
        return mContactDao.getAll();
    }

    public Contact getContact(PeerSemanticTag tag) {
        return mContactDao.get(tag);
    }

    public void addContact(Contact contact) {
        mContactDao.add(contact);
    }

    public void updateContact(Contact contact) {
        mContactDao.update(contact);
    }

    public void removeContact(Contact contact) {
        mContactDao.remove(contact);
    }

    public int numberOfContacts() {
        return mContactDao.size();
    }

    public List<Profile> getProfiles() {
        return mProfileDao.getAll();
    }

    public Profile getProfile(PeerSemanticTag tag) {
        return mProfileDao.get(tag);
    }

    public void addProfile(Profile profile) {
        mProfileDao.add(profile);
    }

    public void updateProfile(Profile profile) {
        mProfileDao.update(profile);
    }

    public void removeProfile(Profile profile) {
        mProfileDao.remove(profile);
    }

    public int numberOfProfiles() {
        return mProfileDao.size();
    }

    @Override
    public void pingMailServer(final SemanticTag type, final PeerSemanticTag receiver) {
        try {
            mEngine.startMail();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ASIPOutMessage message = mEngine.createASIPOutMessage(receiver.getAddresses(), mEngine.getOwner(), receiver, null, null, null, type, 1);
                try {
                    message.expose(InMemoSharkKB.createInMemoASIPInterest());
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    // Shark methods

    public void addRadarListener(NearbyPeerManager.NearbyPeerListener peerListener) {
        mEngine.addNearbyPeerListener(peerListener);
    }

    @Override
    public void allowSyncInvitation(boolean allow) {
        mEngine.getSyncManager().allowInvitation(allow);
    }

    public void startRadar() {
        if (!mIsDiscovering) {
            mEngine.startDiscovery();
            mIsDiscovering = true;
        }
    }

    public void stopRadar() {
        if (mIsDiscovering) {
            mEngine.stopDiscovery();
            mIsDiscovering = false;
        }
    }

    public NfcPkiPortEventListener initNFC(Activity activity) {
        PkiStorage pkiStorage = mEngine.getPKIStorage();
        try {
//            List<SharkCertificate> sharkCertificatesBySigner =
//                    pkiStorage.getSharkCertificatesBySigner(mAccount.getTag());
            ASIPKnowledge knowledge = pkiStorage.getPublicKeyAsKnowledge(true);

            // TODO remove logs
//            ASIPKnowledgeConverter asipKnowledgeConverter = new ASIPKnowledgeConverter(knowledge);
//            L.d("Initial length: " + (asipKnowledgeConverter.getContent().length + asipKnowledgeConverter.getSerializedKnowledge().length()), this);
//            ContactDaoImpl contactDao = new ContactDaoImpl((SharkKB) knowledge);
//            for (SharkCertificate sharkCertificate : sharkCertificatesBySigner) {
//                contactDao.add(getContact(sharkCertificate.getOwner()));
//            }
//            contactDao.add(mAccount);

//            ASIPKnowledgeConverter asipKnowledgeConverter2 = new ASIPKnowledgeConverter(knowledge);
//            L.d("contact added length: " + (asipKnowledgeConverter2.getContent().length + asipKnowledgeConverter2.getSerializedKnowledge().length()), this);
//            L.d("Image length: " + asipKnowledgeConverter2.getContent().length, this);

            NfcPkiPort nfcPkiPort = new NfcPkiPort(mEngine, this, (NfcPkiPortListener) activity);
            mEngine.setupNfc(activity, nfcPkiPort);
            mEngine.stopNfc();
            mEngine.offer(knowledge);
            return nfcPkiPort;
        } catch (SharkProtocolNotSupportedException | SharkNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startNFC() {
        try {
            mEngine.startNfc();
        } catch (SharkProtocolNotSupportedException | IOException e) {
            e.printStackTrace();
        }

    }

    public void stopNFC() {
        try {
            mEngine.stopNfc();
        } catch (SharkProtocolNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initPki() {
        SharkPkiStorage pkiStorage = (SharkPkiStorage) getSharkEngine().getPKIStorage();

        try {
            pkiStorage.setPkiStorageOwner(getAccount().getTag());
            pkiStorage.generateNewKeyPair(1000 * 60 * 60 * 24 * 365);
        } catch (SharkKBException | NoSuchAlgorithmException | IOException e) {
            L.e(e.getMessage());
        }
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}
