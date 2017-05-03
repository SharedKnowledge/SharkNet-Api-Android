package net.sharksystem.api.dao_impl;

import android.app.Activity;
import android.content.Context;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by j4rvis on 4/7/17.
 */

public class ThreadedSharkNetApiImpl implements SharkNetApi {

    private final ExecutorService mExecutorService;
    private final SharkNetApiImpl mApi;

    public ThreadedSharkNetApiImpl(Context context, ExecutorService executorService) {
        mExecutorService = executorService;
        mApi = new SharkNetApiImpl(context);
    }

    @Override
    public AndroidSharkEngine getSharkEngine() {
        return mApi.getSharkEngine();
    }

    @Override
    public Contact getAccount() {
        Future<Contact> future = mExecutorService.submit(new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                return mApi.getAccount();
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setAccount(final Contact contact) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mApi.setAccount(contact);
            }
        });
    }

    @Override
    public List<Chat> getChats() {
        Future<List<Chat>> future = mExecutorService.submit(new Callable<List<Chat>>() {
            @Override
            public List<Chat> call() throws Exception {
                return mApi.getChats();
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Chat getChat(final SemanticTag tag) {
        Future<Chat> future = mExecutorService.submit(new Callable<Chat>() {
            @Override
            public Chat call() throws Exception {
                return mApi.getChat(tag);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addChat(final Chat chat) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mApi.addChat(chat);
            }
        });
    }

    @Override
    public void updateChat(final Chat chat) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mApi.updateChat(chat);
            }
        });
    }

    @Override
    public void removeChat(final Chat chat) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mApi.removeChat(chat);
            }
        });
    }

    @Override
    public int numberOfChats() {
        Future<Integer> future = mExecutorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return mApi.numberOfChats();
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Contact> getContacts() {
        Future<List<Contact>> future = mExecutorService.submit(new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() throws Exception {
                return mApi.getContacts();
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Contact getContact(final PeerSemanticTag tag) {
        Future<Contact> future = mExecutorService.submit(new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                return mApi.getContact(tag);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addContact(final Contact contact) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mApi.addContact(contact);
            }
        });
    }

    @Override
    public void updateContact(final Contact contact) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mApi.updateContact(contact);
            }
        });
    }

    @Override
    public void removeContact(final Contact contact) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mApi.removeContact(contact);
            }
        });
    }

    @Override
    public int numberOfContacts() {
        Future<Integer> future = mExecutorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return mApi.numberOfContacts();
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void pingMailServer(final SemanticTag type, final PeerSemanticTag receiver) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mApi.pingMailServer(type, receiver);
            }
        });
    }

    @Override
    public void addRadarListener(NearbyPeerManager.NearbyPeerListener peerListener){
        mApi.addRadarListener(peerListener);
    }

    @Override
    public void allowSyncInvitation(boolean allow) {
        mApi.allowSyncInvitation(allow);
    }

    @Override
    public void startRadar() {
        mApi.startRadar();
    }

    @Override
    public void stopRadar() {
        mApi.stopRadar();
    }

    @Override
    public NfcPkiPortEventListener initNFC(Activity activity) {
        return mApi.initNFC(activity);
    }

    @Override
    public void startNFC() {
        mApi.startNFC();
    }

    @Override
    public void stopNFC() {
        mApi.stopNFC();
    }

    @Override
    public void initPki() {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mApi.initPki();
            }
        });
    }
}