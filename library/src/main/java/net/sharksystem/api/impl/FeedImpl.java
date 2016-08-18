package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.knowledgeBase.Interest;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSTSet;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.api.interfaces.Comment;
import net.sharksystem.api.interfaces.Contact;
import net.sharksystem.api.interfaces.Content;
import net.sharksystem.api.interfaces.Feed;
import net.sharksystem.api.utils.SharkNetUtils;

import org.json.JSONException;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 14.08.16.
 */
public class FeedImpl implements Feed {

    private static final String FEED_SENDER = "FEED_SENDER";
    private static final String FEED_IS_DISLIKED = "FEED_IS_DISLIKED";
    private static final String FEED_INTEREST = "FEED_INTEREST";
    private static final String FEED_UID = "FEED_UID";


    private static final SemanticTag mCommentType =
            InMemoSharkKB.createInMemoSemanticTag("COMMENT", "http://sharksystem.net/comment");

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
        setId();
    }

    FeedImpl(SharkNetEngine engine, SharkKB kb, ASIPInformationSpace informationSpace) throws SharkKBException {
        mEngine = engine;
        mKb = kb;
        mInformationSpace = informationSpace;

        if(getId().isEmpty()){
            setId();
        }
    }

    @Override
    public void setInterest(Interest interest) throws SharkKBException {
        String s = ASIPSerializer.serializeASIPSpace((ASIPSpace) interest).toString();
        SharkNetUtils.setInfoWithName(mKb, mInformationSpace.getASIPSpace(), FEED_INTEREST, s);
    }

    @Override
    public Interest getInterest() throws SharkKBException {
        ASIPInformation information =
                SharkNetUtils.getInfoByName(mKb, mInformationSpace.getASIPSpace(), FEED_INTEREST);
        if(information!=null){
            return (Interest) ASIPSerializer.deserializeASIPInterest(information.getContentAsString());
        }
        return null;
    }

    private void setId() throws SharkKBException {
        String name = getSender().getName();
        String si = getSender().getPST().getSI()[0];
        int timestamp = getTimestamp().getNanos();

        SharkNetUtils.setInfoWithName(
                mKb,
                mInformationSpace.getASIPSpace(),
                FEED_UID,
                name + "_" + si + "_" + timestamp
        );
    }

    public String getId() throws SharkKBException {
        ASIPInformation information = SharkNetUtils.getInfoByName(mKb, mInformationSpace.getASIPSpace(), FEED_UID);
        if(information!=null){
            return information.getContentAsString();
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

//    TODO setSender unnecessary
    @Override
    public void setSender(Contact sender) throws SharkKBException, JSONException {
        PeerSemanticTag tag = sender.getPST();
        String s = ASIPSerializer.serializeTag(tag).toString();
        SharkNetUtils.setInfoWithName(mKb, mInformationSpace.getASIPSpace(), FEED_SENDER, s);
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

//    TODO newComment with Content
    @Override
    public Comment newComment(Content comment, Contact author) {
        return null;
    }

    public Comment newComment(InputStream stream, String message, String mimeType) throws SharkKBException, JSONException {
        ASIPSpace currentTimeSpace = SharkNetUtils.createCurrentTimeSpace(mEngine.getCommentsKB(), mCommentType);
        CommentImpl comment = new CommentImpl(mEngine, mEngine.getCommentsKB(), currentTimeSpace, this);
        comment.setContent(stream, message, mimeType);
        return comment;
    }

    public List<Comment> getComments() throws SharkKBException {
        List<Comment> comments = new ArrayList<>();
        Iterator<ASIPInformationSpace> iterator = mEngine.getCommentsKB().informationSpaces();
        while (iterator.hasNext()){
            ASIPInformationSpace next = iterator.next();
            if(next == null){
                continue;
            }
            // checks if the infoSpace has an type SemanticTag with the SI from the mMessageType-Tag
            STSet types = next.getASIPSpace().getTypes();
            if(types.getSemanticTag(mCommentType.getSI())!=null){
                CommentImpl comment = new CommentImpl(mEngine, mEngine.getCommentsKB(), next);
                comments.add(comment);
            }
        }
        return comments;
    }

    @Override
    public List<Comment> getComments(boolean ascending) throws SharkKBException {
        return (List<Comment>) SharkNetUtils.sortList(getComments(), ascending);
    }

    @Override
    public List<Comment> getComments(int startIndex, int stopIndex, boolean ascending) throws SharkKBException {
        List<Comment> list = (List<Comment>) SharkNetUtils.cutList(getComments(), startIndex, stopIndex);
        return (List<Comment>) SharkNetUtils.sortList(list, ascending);
    }

    @Override
    public List<Comment> getComments(Timestamp start, Timestamp stop, boolean ascending) throws SharkKBException {
        List<Comment> list = (List<Comment>) SharkNetUtils.cutList(getComments(), start, stop);
        return (List<Comment>) SharkNetUtils.sortList(list, ascending);
    }

    @Override
    public List<Comment> getComments(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean ascending) throws SharkKBException {
        List<Comment> listTimestamp = (List<Comment>) SharkNetUtils.cutList(getComments(), start, stop);
        List<Comment> listCuted = (List<Comment>) SharkNetUtils.cutList(listTimestamp, startIndex, stopIndex);
        return (List<Comment>) SharkNetUtils.sortList(listCuted, ascending);
    }

    @Override
    public List<Comment> getComments(String search, int startIndex, int stopIndex, boolean ascending) throws SharkKBException {
        List<Comment> listTimestamp = (List<Comment>) SharkNetUtils.cutList(getComments(), startIndex, stopIndex);
        List<Comment> searched = (List<Comment>) SharkNetUtils.search(search, listTimestamp);
        return (List<Comment>) SharkNetUtils.sortList(searched, ascending);
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
