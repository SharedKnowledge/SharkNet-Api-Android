package net.sharksystem.api.impl;

import net.sharksystem.api.interfaces.Comment;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Feed;

import java.io.InputStream;
import java.sql.Timestamp;

/**
 * Created by j4rvis on 14.08.16.
 */
public class CommentImpl implements Comment {

    @Override
    public Contact getSender() {
        return null;
    }

    @Override
    public Timestamp getTimestamp() {
        return null;
    }

    @Override
    public Feed getRefFeed() {
        return null;
    }

    @Override
    public void setContent(InputStream stream, String message, String mimeType) {

    }

    @Override
    public Content getContent() {
        return null;
    }

    @Override
    public void delete() {

    }

    @Override
    public void setDisliked(boolean isDisliked) {

    }

    @Override
    public boolean isDisliked() {
        return false;
    }
}
