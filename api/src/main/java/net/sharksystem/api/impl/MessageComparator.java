package net.sharksystem.api.impl;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Message;

import java.util.Comparator;

/**
 * Created by j4rvis on 13.08.16.
 */
public class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message lhs, Message rhs) {
        try {
            if (lhs.getDateReceived().before(rhs.getDateReceived())) {
                return -1;
            } else if (lhs.getDateReceived().equals(rhs.getDateReceived())) {
                return 0;
            } else if (lhs.getDateReceived().after(rhs.getDateReceived())) {
                return 1;
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
