package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.dummy_impl.ImplVoting;
import net.sharksystem.api.dummy_impl.Reminder;
import net.sharksystem.api.utils.ClassHelper;
import net.sharksystem.api.utils.SharkNetUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by j4rvis on 02.08.16.
 */
public class ContentImpl implements Content {

    public final static String CONTENT_INFO = "CONTENT_INFO";
    public final static String CONTENT_MESSAGE = "CONTENT_MESSAGE";

    private final SharkKB mSharkKB;
    private final ASIPSpace mSpace;


    ContentImpl(SharkKB sharkKB, ASIPSpace space) {
        mSharkKB = sharkKB;
        mSpace = space;
    }

    @Override
    public boolean setInputStream(InputStream is) throws SharkKBException {
        try {
            SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTENT_INFO, is);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setMimeType(String mimeType) throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTENT_INFO);
        if(information!=null){
            information.setContentType(mimeType);
        }
//        SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTENT_INFO, mimeType);
    }

    @Override
    public String getMimeType() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTENT_INFO);
        if(information!=null){
            return information.getContentType();
        }
        return null;
    }

    @Override
    public InputStream getInputStream() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTENT_INFO);
        if(information!=null){
            return new ByteArrayInputStream(information.getContentAsByte());
        }
        return null;
    }

    @Override
    public void setMessage(String message) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mSharkKB, mSpace, CONTENT_MESSAGE, message);
    }

    @Override
    public String getMessage() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTENT_MESSAGE);
        if(information!=null){
            return information.getContentAsString();
        }
        return null;
    }

    @Override
    public long getLength() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mSpace, CONTENT_INFO);
        if(information!=null){
            return information.getContentLength();
        }
        return 0;
    }

    // TODO Voting and Reminder

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        try {
            return ClassHelper.equals(Content.class, this, o);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
