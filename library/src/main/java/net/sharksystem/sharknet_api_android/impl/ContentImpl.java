package net.sharksystem.sharknet_api_android.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.sharknet_api_android.interfaces.Content;
import net.sharksystem.sharknet_api_android.dummy_impl.ImplVoting;
import net.sharksystem.sharknet_api_android.dummy_impl.Reminder;
import net.sharksystem.sharknet_api_android.utils.SharkNetUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * Created by j4rvis on 02.08.16.
 */
public class ContentImpl implements Content {

    public final static String CONTENT_INFO = "CONTENT_INFO";
    public final static String CONTENT_MESSAGE = "CONTENT_MESSAGE";

    private final SharkKB mSharkKB;
    private final ASIPSpace mASIPSpace;


    ContentImpl(SharkKB sharkKB, ASIPSpace space) {
        mSharkKB = sharkKB;
        mASIPSpace = space;
    }

    @Override
    public boolean setInputStream(InputStream is) throws SharkKBException {
        try {
            ASIPInformation asipInformation = mSharkKB.addInformation(is, is.available(), mASIPSpace);
            asipInformation.setName(CONTENT_INFO);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OutputStream getOutputStream() throws SharkKBException {
        // TODO
        return null;
    }

    @Override
    public String getMimeType() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mASIPSpace, CONTENT_INFO);
        if(information!=null){
            return information.getContentType();
        }
        return null;
    }

    @Override
    public void setMimeType(String mimeType) throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mASIPSpace, CONTENT_INFO);
        if(information!=null){
            information.setContentType(mimeType);
        }
    }

    @Override
    public InputStream getInputStream() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mASIPSpace, CONTENT_INFO);
        if(information!=null){
            return new ByteArrayInputStream(information.getContentAsByte());
        }
        return null;
    }

    @Override
    public String getMessage() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mASIPSpace, CONTENT_MESSAGE);
        if(information!=null){
            return information.getContentAsString();
        }
        return null;
    }

    @Override
    public void setMessage(String message) throws SharkKBException {
        ASIPInformation information = mSharkKB.addInformation(message, mASIPSpace);
        if(information!=null){
            information.setName(CONTENT_MESSAGE);
        }

    }

    // TODO

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
