package net.sharksystem.api.dao_impl;

import android.os.Handler;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.sync.SyncKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.knowledgeBase.sync.manager.SyncManager;
import net.sharkfw.knowledgeBase.sync.manager.port.SyncMergeKP;
import net.sharkfw.peer.J2SESharkEngine;
import net.sharkfw.system.L;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by j4rvis on 5/9/17.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SharkNetImplTest {

    @Test
    public void sync_forceConcurrentException_success() throws SharkKBException, InterruptedException, IOException {

        L.setLogLevel(L.LOGLEVEL_WARNING);

        // Basics
        SharkNetApiImpl aliceApi = new SharkNetApiImpl(InstrumentationRegistry.getContext());
        J2SESharkEngine aliceEngine = aliceApi.getSharkEngine();
        SyncManager aliceManager = aliceEngine.getSyncManager();

        // Create alice
        PeerSemanticTag alice = InMemoSharkKB.createInMemoPeerSemanticTag("alice", "alice.de", "tcp://localhost:7070");
        aliceEngine.setEngineOwnerPeer(alice);

        // Create bob
        PeerSemanticTag bob = InMemoSharkKB.createInMemoPeerSemanticTag("bob", "bob.de", "tcp://localhost:7072");

        // Create a kb to share
        InMemoSharkKB sharkKB = new InMemoSharkKB();
        sharkKB.addInformation("This is just \"an example\"!!!", InMemoSharkKB.createInMemoASIPInterest());
        sharkKB.addInformation("This is just \"another example\"!!!", InMemoSharkKB.createInMemoASIPInterest());
        sharkKB.addInformation("This is just \"anothasder example\"!!!", InMemoSharkKB.createInMemoASIPInterest());
        sharkKB.addInformation("This is just \"anothasfer easfasxample\"!!!", InMemoSharkKB.createInMemoASIPInterest());
        sharkKB.addInformation("This is just \"anotherasfasfa example\"!!!", InMemoSharkKB.createInMemoASIPInterest());
        SemanticTag kbName = sharkKB.createInMemoSemanticTag("kbName", "kbsi.de");

        // Now create the component

        final SyncComponent component = aliceManager.createSyncComponent(sharkKB, kbName, bob, alice, true);

        final Handler handler = new Handler(Looper.getMainLooper());

        Log.d("TEST", "1");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SyncKB kb = component.getKb();
                try {
                    kb.addInformation("This is just \"an example\"!!!", InMemoSharkKB.createInMemoASIPInterest());
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
                Log.d("RUNNABLE", "Running");
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);

        Thread.sleep(1000);

        aliceApi.getChats();

        Thread.sleep(3000);
    }
}
