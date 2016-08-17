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
            if (lhs.getTimestamp().before(rhs.getTimestamp())) {
                return -1;
            } else if (lhs.getTimestamp().equals(rhs.getTimestamp())) {
                return 0;
            } else if (lhs.getTimestamp().after(rhs.getTimestamp())) {
                return 1;
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
