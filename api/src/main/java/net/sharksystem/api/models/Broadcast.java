package net.sharksystem.api.models;

import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dustin Feurich
 */

public class Broadcast {

    public final static String BROADCAST_ID = "BROADCAST_ID";
    public static final String BROADCAST = "Broadcast";

    private SemanticTag id;
    private List<Message> messages = new ArrayList<>();

    public Broadcast() {
        id = InMemoSharkKB.createInMemoSemanticTag(BROADCAST_ID, BROADCAST);
        messages = new ArrayList<>();
    }

    public SemanticTag getId() {
        return id;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }

}
