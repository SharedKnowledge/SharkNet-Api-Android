package net.sharksystem.api.utils;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.api.impl.SharkNetEngine;
import net.sharksystem.api.interfaces.Chat;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Profile;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by j4rvis on 24.08.16.
 */
public class DummyCreator {

    public static void createDummyData() throws SharkKBException, JSONException {

        String aliceName = "Alice";
        String aliceNickName = "Ali";
        String aliceSI = "www.facebook.com/alice";
        String aliceMail = "mail://alice.com";
        String aliceTelephoneHome = "030 123456789";
        String aliceTelephoneMobile = "01177 123456789";
        String aliceNote = "This is just a simple note";

        String bobName = "Bob";
        String bobNickName = "Bob";
        String bobSI = "www.facebook.com/bob";
        String bobMail = "mail://bob.com";
        String bobTelephoneHome = "030 1234567891";
        String bobTelephoneMobile = "01177 1234567891";
        String bobNote = "This is just a simple note from Bob";

        String charlieName = "Charlie";
        String charlieNickName = "Charlie";
        String charlieSI = "www.facebook.com/charlie";
        String charlieMail = "mail://charlie.com";
        String charlieTelephoneHome = "030 1234567891";
        String charlieTelephoneMobile = "01177 1234567891";
        String charlieNote = "This is just a simple note from Charlie";

        String davidName = "David";
        String davidNickName = "Dave";
        String davidSI = "www.facebook.com/david";
        String davidMail = "mail://david.com";
        String davidTelephoneHome = "030 1234567891";
        String davidTelephoneMobile = "01177 1234567891";
        String davidNote = "This is just a simple note from David";

        String eliseName = "Elise";
        String eliseNickName = "Elli";
        String eliseSI = "www.facebook.com/elise";
        String eliseMail = "mail://elise.com";
        String eliseTelephoneHome = "030 1234567891";
        String eliseTelephoneMobile = "01177 1234567891";
        String eliseNote = "This is just a simple note from Elise";


        byte[] randomByte = new byte[20];
        InputStream stream = new ByteArrayInputStream(randomByte);
        
        SharkNetEngine engine = SharkNetEngine.getSharkNet();

        // Create contacts

        Contact alice = engine.newContact(aliceName, aliceSI);
        alice.setEmail(aliceMail);
        alice.setNickname(aliceNickName);
        alice.addTelephoneNumber(aliceTelephoneHome);
        alice.addTelephoneNumber(aliceTelephoneMobile);
        alice.addNote(aliceNote);

        Contact bob = engine.newContact(bobName, bobSI);
        bob.setEmail(bobMail);
        bob.setNickname(bobNickName);
        bob.addTelephoneNumber(bobTelephoneHome);
        bob.addTelephoneNumber(bobTelephoneMobile);
        bob.addNote(bobNote);

        Contact charlie = engine.newContact(charlieName, charlieSI);
        charlie.setEmail(charlieMail);
        charlie.setNickname(charlieNickName);
        charlie.addTelephoneNumber(charlieTelephoneHome);
        charlie.addTelephoneNumber(charlieTelephoneMobile);
        charlie.addNote(charlieNote);

        Profile david = engine.newProfile(davidName, davidSI);
        david.setEmail(davidMail);
        david.setNickname(davidNickName);
        david.addTelephoneNumber(davidTelephoneHome);
        david.addTelephoneNumber(davidTelephoneMobile);
        david.addNote(davidNote);

        Profile elise = engine.newProfile(eliseName, eliseSI);
        elise.setEmail(eliseMail);
        elise.setNickname(eliseNickName);
        elise.addTelephoneNumber(eliseTelephoneHome);
        elise.addTelephoneNumber(eliseTelephoneMobile);
        elise.addNote(eliseNote);

        engine.setActiveProfile(david, "password");

        ArrayList<Contact> aliceAndBob = new ArrayList<>();
        aliceAndBob.add(alice);
        aliceAndBob.add(bob);

        ArrayList<Contact> aliceAndBobAndCharlie = new ArrayList<>();
        aliceAndBobAndCharlie.add(alice);
        aliceAndBobAndCharlie.add(bob);
        aliceAndBobAndCharlie.add(charlie);

        ArrayList<Contact> aliceAndCharlie = new ArrayList<>();
        aliceAndCharlie.add(alice);
        aliceAndCharlie.add(charlie);

        ArrayList<Contact> bobAndCharlie = new ArrayList<>();
        bobAndCharlie.add(bob);
        bobAndCharlie.add(charlie);

        // Chat initiation

        Chat aliceAndBobChat = engine.newChat(aliceAndBob);
        aliceAndBobChat.setTitle("ABD");
        Chat aliceAndBobAndCharlieChat = engine.newChat(aliceAndBobAndCharlie);
        aliceAndBobAndCharlieChat.setTitle("ABCD");
        Chat aliceAndCharlieChat = engine.newChat(aliceAndCharlie);
        aliceAndCharlieChat.setTitle("ACD");
        Chat bobAndCharlieChat = engine.newChat(bobAndCharlie);
        bobAndCharlieChat.setTitle("BCD");

        aliceAndBobChat.sendMessage(null, "Hallo Bob und David", null, alice);
        aliceAndBobChat.sendMessage(null, "Wie geht es euch beiden?", null, alice);
        aliceAndBobChat.sendMessage(null, "Hi mir geht es gut und dir?", null, bob);
        aliceAndBobChat.sendMessage(null, "Mir geht es auch gut! Was macht ihr so?", null);
        aliceAndBobChat.sendMessage(null, "Bestens ja! Ich bin gerade am SharkNet testen :D", null, alice);

        aliceAndBobAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut", null);
        aliceAndBobAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut", null, alice);
        aliceAndBobAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut", null, bob);
        aliceAndBobAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut", null, charlie);
        aliceAndBobAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut", null);

        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null, alice);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null, charlie);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null, alice);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null, charlie);
        aliceAndCharlieChat.sendMessage(null, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr" +
                ", sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed " +
                "diam voluptua. At vero eos et accusam et", null, alice);

        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor consetetur sadipscing elitr", null, bob);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null, charlie);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null, bob);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null, bob);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null, charlie);
        bobAndCharlieChat.sendMessage(null, "sed diam nonumy eirmod tempor ", null);

    }
}
