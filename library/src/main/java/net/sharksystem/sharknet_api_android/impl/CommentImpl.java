package net.sharksystem.sharknet_api_android.impl;

import net.sharksystem.sharknet_api_android.interfaces.Comment;
import net.sharksystem.sharknet_api_android.interfaces.Contact;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.interfaces.Feed;

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
