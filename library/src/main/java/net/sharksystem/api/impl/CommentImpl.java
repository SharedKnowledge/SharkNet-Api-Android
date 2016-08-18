package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSTSet;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharksystem.api.interfaces.Comment;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.api.utils.SharkNetUtils;

import org.json.JSONException;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Iterator;

/**
 * Created by j4rvis on 14.08.16.
 */
public class CommentImpl implements Comment {

    private static final String COMMENT_SENDER = "COMMENT_SENDER";
    private static final String COMMENT_REF_FEED = "COMMENT_REF_FEED";

    private final SharkNetEngine mEngine;
    private final SharkKB mSharkKB;
    private final Feed mFeed;
    private ASIPInformationSpace mInformationSpace;

    CommentImpl(SharkNetEngine engine, SharkKB kb, ASIPSpace space, Feed feed) throws JSONException, SharkKBException {
        mEngine = engine;
        mSharkKB = kb;
        mFeed = feed;

        // There should not be an InformationSpace yet, so create one by adding the first information
        // by setting the sender
        PeerSemanticTag owner = mSharkKB.getOwner();
        String serializedOwner = ASIPSerializer.serializeTag(owner).toString();
        SharkNetUtils.setInfoWithName(mSharkKB, space, COMMENT_SENDER, serializedOwner);

        // now search for the desired InformationSpace and set it.
        Iterator<ASIPInformationSpace> informationSpaces = mSharkKB.informationSpaces(space, true);
        if(informationSpaces.hasNext()){
            mInformationSpace = informationSpaces.next();
        }
    }

    CommentImpl(SharkNetEngine engine, SharkKB kb, ASIPInformationSpace informationSpace, Feed feed){
        mEngine = engine;
        mSharkKB = kb;
        mInformationSpace = informationSpace;
        mFeed = feed;
    }

    @Override
    public Contact getSender() throws SharkKBException {
        ASIPInformation information =
                SharkNetUtils.getInfoByName(mSharkKB, mInformationSpace.getASIPSpace(), COMMENT_SENDER);
        if(information!=null){
            PeerSemanticTag tag = ASIPSerializer.deserializePeerTag(information.getContentAsString());
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
