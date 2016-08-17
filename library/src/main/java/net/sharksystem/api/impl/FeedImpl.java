package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.knowledgeBase.Interest;
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
import java.util.List;

/**
 * Created by j4rvis on 14.08.16.
 */
public class FeedImpl implements Feed {

    public static final String FEED_SENDER = "FEED_SENDER";
    public static final String FEED_IS_DISLIKED = "FEED_IS_DISLIKED";

    private final SharkNetEngine mEngine;
    private final SharkKB mKb;
    private ASIPInformationSpace mInformationSpace;

    FeedImpl(SharkNetEngine engine, SharkKB kb, ASIPSpace space) throws JSONException, SharkKBException {
        mEngine = engine;
        mKb = kb;

        PeerSemanticTag owner = mKb.getOwner();
        String serializedOwner = ASIPSerializer.serializeTag(owner).toString();
        SharkNetUtils.setInfoWithName(mKb, space, FEED_SENDER, serializedOwner);

        // now search for the desired InformationSpace and set it.
        Iterator<ASIPInformationSpace> informationSpaces = mKb.informationSpaces(space, true);
        if(informationSpaces.hasNext()){
            mInformationSpace = informationSpaces.next();
        }
    }

    FeedImpl(SharkNetEngine engine, SharkKB kb, ASIPInformationSpace informationSpace){
        mEngine = engine;
        mKb = kb;
        mInformationSpace = informationSpace;
    }

//    TODO setInterest - we just need the Topics, but always treat as ASIPInterest?
    @Override
    public void setInterest(Interest interest) {

    }

//    TODO getInterest
    @Override
    public Interest getInterest() {
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
    public void setContent(InputStream inputStream, String messageString, String mimeType) throws SharkKBException {
        ContentImpl content = new ContentImpl(mKb, mInformationSpace.getASIPSpace());
        content.setMimeType(mimeType);
        content.setInputStream(inputStream);
        content.setMessage(messageString);
    }

    @Override
    public Content getContent() throws SharkKBException {
        return new ContentImpl(mKb, mInformationSpace.getASIPSpace());
    }

    @Override
    public Contact getSender() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mKb, mInformationSpace.getASIPSpace(), FEED_SENDER);
        if(information!=null){
            String informationContentAsString = information.getContentAsString();
            PeerSemanticTag sender = ASIPSerializer.deserializePeerTag(informationContentAsString);
            return mEngine.getContactByTag(sender);
        }
        return null;
    }

    @Override
    public void delete() throws SharkKBException {
        mKb.removeInformation(mInformationSpace.getASIPSpace());
    }

//    TODO newComment
    @Override
    public void newComment(Content comment, Contact author) {

    }

//    TODO getComments - use static helper methods in SharkNetUtils
    @Override
    public List<Comment> getComments(boolean descending) throws SharkKBException {
        return null;
    }

    @Override
    public List<Comment> getComments(int startIndex, int stopIndex, boolean descending) throws SharkKBException {
        return null;
    }

    @Override
    public List<Comment> getComments(Timestamp start, Timestamp stop, boolean descending) throws SharkKBException {
        return null;
    }

    @Override
    public List<Comment> getComments(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean descending) throws SharkKBException {
        return null;
    }

    @Override
    public List<Comment> getComments(String search, int startIndex, int stopIndex, boolean descending) throws SharkKBException {
        return null;
    }

    @Override
    public void setDisliked(boolean disliked) throws SharkKBException {
        SharkNetUtils.setInfoAsBooleanString(mKb, mInformationSpace.getASIPSpace(), FEED_IS_DISLIKED, disliked);
    }

    @Override
    public boolean isDisliked() throws SharkKBException {
        return SharkNetUtils.getInfoAsBoolean(mKb, mInformationSpace.getASIPSpace(), FEED_IS_DISLIKED);
    }
}
