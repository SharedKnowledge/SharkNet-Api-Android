package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.serialization.ASIPMessageSerializerHelper;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSTSet;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharksystem.api.interfaces.Comment;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.api.utils.ClassHelper;
import net.sharksystem.api.utils.SharkNetUtils;

import org.json.JSONException;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Iterator;

/**
 * Created by j4rvis on 14.08.16.
 */
public class CommentImpl implements Comment {

    private static final String COMMENT_SENDER = "COMMENT_SENDER";
    private static final String COMMENT_REF_FEED = "COMMENT_REF_FEED";
    private static final String COMMENT_IS_DISLIKED = "COMMENT_IS_DISLIKED";

    private final SharkNetEngine mEngine;
    private final SharkKB mSharkKB;
    private ASIPInformationSpace mInformationSpace;

    CommentImpl(SharkNetEngine engine, SharkKB kb, ASIPSpace space, Feed feed) throws JSONException, SharkKBException {
        mEngine = engine;
        mSharkKB = kb;

        // There should not be an InformationSpace yet, so create one by adding the first information
        // by setting the sender
        PeerSemanticTag owner = mSharkKB.getOwner();
        String serializedOwner = ASIPMessageSerializerHelper.serializeTag(owner).toString();
        SharkNetUtils.setInfoWithName(mSharkKB, space, COMMENT_SENDER, serializedOwner);

        // now search for the desired InformationSpace and set it.
        Iterator<ASIPInformationSpace> informationSpaces = mSharkKB.informationSpaces(space, true);
        if(informationSpaces.hasNext()){
            mInformationSpace = informationSpaces.next();
        }

        SharkNetUtils.setInfoWithName(mSharkKB, mInformationSpace.getASIPSpace(), COMMENT_REF_FEED, feed.getId());
    }

    CommentImpl(SharkNetEngine engine, SharkKB kb, ASIPInformationSpace informationSpace){
        mEngine = engine;
        mSharkKB = kb;
        mInformationSpace = informationSpace;
    }

    @Override
    public Contact getSender() throws SharkKBException {
        ASIPInformation information =
                SharkNetUtils.getInfoByName(mSharkKB, mInformationSpace.getASIPSpace(), COMMENT_SENDER);
        if(information!=null){
            PeerSemanticTag tag = ASIPMessageSerializerHelper.deserializePeerTag(information.getContentAsString());
            return mEngine.getContactByTag(tag);
        }
        return null;
    }

    @Override
    public Timestamp getTimestamp() throws SharkKBException {
        TimeSTSet times = mInformationSpace.getASIPSpace().getTimes();
        Iterator<TimeSemanticTag> timeSemanticTagIterator = times.tstTags();
        if(timeSemanticTagIterator.hasNext()) {
            TimeSemanticTag next = timeSemanticTagIterator.next();
            long from = next.getFrom();
            return new Timestamp(from);
        }
        return null;
    }

    @Override
    public Feed getRefFeed() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKB, mInformationSpace.getASIPSpace(), COMMENT_REF_FEED);
        if(information!=null){
            return mEngine.getFeedById(information.getContentAsString());
        }
        return null;
    }

    @Override
    public void setContent(InputStream stream, String message, String mimeType) throws SharkKBException {
        ContentImpl content = new ContentImpl(mSharkKB, mInformationSpace.getASIPSpace());
        content.setMimeType(mimeType);
        content.setInputStream(stream);
        content.setMessage(message);
    }

    @Override
    public Content getContent() throws SharkKBException {
        return new ContentImpl(mSharkKB, mInformationSpace.getASIPSpace());
    }

    @Override
    public void delete() throws SharkKBException {
        mSharkKB.removeInformation(mInformationSpace.getASIPSpace());
    }
    @Override
    public void setDisliked(boolean disliked) throws SharkKBException {
        SharkNetUtils.setInfoWithName(mSharkKB, mInformationSpace.getASIPSpace(), COMMENT_IS_DISLIKED, disliked);
    }

    @Override
    public boolean isDisliked() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mSharkKB, mInformationSpace.getASIPSpace(), COMMENT_IS_DISLIKED);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        try {
            return ClassHelper.equals(Comment.class, this, o);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
