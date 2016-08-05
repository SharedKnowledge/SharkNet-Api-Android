package net.sharksystem.sharknet_api_android.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.knowledgeBase.inmemory.InMemoInformation;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.dummy_impl.ImplVoting;
import net.sharksystem.sharknet_api_android.dummy_impl.Reminder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by j4rvis on 02.08.16.
 */
public class ContentImpl implements Content {

    private final ASIPInformation information;

    ContentImpl(ASIPInformation information) {
        this.information = information;
    }

    @Override
    public boolean setInputstream(InputStream is) {
        try {
            this.information.setContent(is, is.available());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OutputStream getOutputstream() {
        return null;
    }

    @Override
    public String getMimeType() {
        return this.information.getContentType();
    }

    @Override
    public void setMimeType(String mimeType) {
        this.information.setContentType(mimeType);
    }

    @Override
    public InputStream getInputstream() {
        return new ByteArrayInputStream(this.information.getContentAsByte());
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public void setMessage(String message) {

    }

    @Override
    public ImplVoting addVoting(String question, boolean singleqoice) {
        return null;
    }

    @Override
    public ImplVoting getVoting() {
        return null;
    }

    @Override
    public Reminder getReminder() {
        return null;
    }

    @Override
    public void setReminder(Reminder reminder) {

    }

    @Override
    public int getLength() {
        return 0;
    }
}
