package net.sharksystem.api.models;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by j4rvis on 3/22/17.
 */

public class Message {
    private String message;
    private Bitmap imageMessage;
    private Date date;
    private Contact sender;
    private boolean isVerified;
    private boolean isSigned;
    private boolean isEncrypted;
    private boolean isMine;
}
