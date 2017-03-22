package net.sharksystem.api.models;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by j4rvis on 3/22/17.
 */

public class Chat {
    private List<Message> messageList;
    private List<Contact> contactList;
    private Contact owner;
    private String title;
    private Bitmap image;
}
